import { CommonModule, CurrencyPipe, DecimalPipe } from '@angular/common';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';

interface Employee { id: number; employeeCode: string; firstName: string; lastName: string; email: string; department: string; country: string; jobTitle: string; currency: string; salary: number; updatedAt: string; }
interface EmployeePage { items: Employee[]; totalItems: number; page: number; size: number; totalPages: number; }
interface Summary { currency: string; employeeCount: number; averageSalary: number; minimumSalary: number; maximumSalary: number; }
interface FilterOptions { countries: string[]; departments: string[]; }

@Component({ selector: 'app-root', standalone: true, imports: [CommonModule, FormsModule, DecimalPipe, CurrencyPipe], template: `
<div class="app-shell">
  <header class="topbar"><div class="brand-mark">SM</div><div><div class="brand-title">Salary Management</div><div class="brand-subtitle">People operations · Compensation overview</div></div><span class="header-pill">HR WORKSPACE</span></header>
  <main>
    <section class="intro"><div><p class="eyebrow">OVERVIEW</p><h1>Employee compensation</h1><p class="muted">Explore employee records and salary insights across your organization.</p></div><button class="secondary-button" (click)="refresh()">↻ Refresh data</button></section>
    <div class="notice" *ngIf="error">{{ error }}</div>
    <section class="summary-grid" *ngIf="summaries.length; else summaryLoading">
      <article class="summary-card"><div class="card-label">Employees in view</div><div class="big-number">{{ totalItems | number }}</div><div class="card-foot">Matching current filters</div></article>
      <article class="summary-card" *ngFor="let s of summaries"><div class="card-label">Average salary · {{s.currency}}</div><div class="big-number">{{s.averageSalary | number:'1.0-0'}}</div><div class="card-foot">{{s.employeeCount | number}} employees · {{s.currency}}</div><div class="range-line"><span>Min {{s.minimumSalary | number:'1.0-0'}}</span><span>Max {{s.maximumSalary | number:'1.0-0'}}</span></div></article>
    </section>
    <ng-template #summaryLoading><div class="loading-card">Loading salary insights…</div></ng-template>
    <section class="directory-card">
      <div class="section-heading"><div><h2>Employee directory</h2><p class="muted">Search, filter and maintain employee salary records.</p></div><span class="result-count">{{ totalItems | number }} records</span></div>
      <div class="filters">
        <label class="search-box"><span>⌕</span><input [(ngModel)]="search" (ngModelChange)="onFilterChange()" placeholder="Search name, employee ID or email" /></label>
        <select [(ngModel)]="country" (ngModelChange)="onFilterChange()"><option value="">All countries</option><option *ngFor="let c of options.countries" [value]="c">{{c}}</option></select>
        <select [(ngModel)]="department" (ngModelChange)="onFilterChange()"><option value="">All departments</option><option *ngFor="let d of options.departments" [value]="d">{{d}}</option></select>
        <button class="clear-button" (click)="clearFilters()">Clear</button>
      </div>
      <div class="table-wrap" *ngIf="!loading; else tableLoading">
        <table><thead><tr><th>EMPLOYEE</th><th>DEPARTMENT</th><th>COUNTRY</th><th>JOB TITLE</th><th class="right">SALARY</th><th></th></tr></thead>
          <tbody><tr *ngFor="let e of employees"><td><div class="person"><span class="avatar">{{ initials(e) }}</span><span><strong>{{e.firstName}} {{e.lastName}}</strong><small>{{e.employeeCode}} · {{e.email}}</small></span></div></td><td>{{e.department}}</td><td>{{e.country}}</td><td>{{e.jobTitle}}</td><td class="right salary-cell">{{e.salary | number:'1.2-2'}} <small>{{e.currency}}</small></td><td><button class="text-button" (click)="openEditor(e)">Edit salary</button></td></tr>
          <tr *ngIf="employees.length===0"><td colspan="6" class="empty">No employees match these filters.</td></tr></tbody></table>
      </div>
      <ng-template #tableLoading><div class="loading-card">Loading employee records…</div></ng-template>
      <footer class="table-footer"><span>Page {{page+1}} of {{totalPages || 1}}</span><div class="pager"><button [disabled]="page===0" (click)="changePage(-1)">← Previous</button><button [disabled]="page+1>=totalPages" (click)="changePage(1)">Next →</button></div></footer>
    </section>
    <div class="modal-backdrop" *ngIf="editing"><section class="modal"><button class="close" (click)="editing=null">×</button><p class="eyebrow">UPDATE COMPENSATION</p><h2>{{editing?.firstName}} {{editing?.lastName}}</h2><p class="muted">{{editing?.employeeCode}} · {{editing?.country}}</p><label class="field-label">Annual salary ({{editing?.currency}})<input type="number" min="0" step="0.01" [(ngModel)]="editSalary" /></label><p class="form-error" *ngIf="editError">{{editError}}</p><div class="modal-actions"><button class="secondary-button" (click)="editing=null">Cancel</button><button class="primary-button" [disabled]="saving" (click)="saveSalary()">{{saving?'Saving…':'Save salary'}}</button></div></section></div>
    <footer class="page-footer">Salary Management · Assessment demo · Synthetic sample data</footer>
  </main>
</div>
` })
export class AppComponent implements OnInit {
  private http = inject(HttpClient);
  employees: Employee[] = []; summaries: Summary[] = []; options: FilterOptions = { countries: [], departments: [] };
  search = ''; country = ''; department = ''; page = 0; size = 20; totalItems = 0; totalPages = 0;
  loading = false; saving = false; error = ''; editing: Employee | null = null; editSalary = 0; editError = '';
  ngOnInit(): void { this.http.get<FilterOptions>('/api/meta/filters').subscribe({ next: x => this.options = x, error: () => this.error = 'Could not load filter options. Is the backend running?' }); this.refresh(); }
  refresh(): void { this.loadEmployees(); this.loadSummary(); }
  onFilterChange(): void { this.page = 0; this.refresh(); }
  clearFilters(): void { this.search=''; this.country=''; this.department=''; this.page=0; this.refresh(); }
  changePage(delta: number): void { this.page += delta; this.loadEmployees(); }
  initials(e: Employee): string { return (e.firstName[0] + e.lastName[0]).toUpperCase(); }
  private params(includePage: boolean): HttpParams { let p = new HttpParams(); if (includePage) p = p.set('page', this.page).set('size', this.size); if (this.search.trim()) p = p.set('search', this.search.trim()); if (this.country) p = p.set('country', this.country); if (this.department) p = p.set('department', this.department); return p; }
  private loadEmployees(): void { this.loading = true; this.error=''; this.http.get<EmployeePage>('/api/employees', {params:this.params(true)}).subscribe({next: r => {this.employees=r.items; this.totalItems=r.totalItems; this.totalPages=r.totalPages; this.loading=false;}, error: () => {this.loading=false; this.error='Could not load employees. Check that the API is running.';}}); }
  private loadSummary(): void { this.http.get<Summary[]>('/api/salary/summary', {params:this.params(false)}).subscribe({next:r=>this.summaries=r,error:()=>this.error='Could not load salary summaries.'}); }
  openEditor(e: Employee): void { this.editing=e; this.editSalary=Number(e.salary); this.editError=''; }
  saveSalary(): void { if (!this.editing) return; if (!Number.isFinite(this.editSalary) || this.editSalary < 0 || Math.round(this.editSalary*100)!==this.editSalary*100) {this.editError='Enter a non-negative salary with at most two decimal places.';return;} this.saving=true; this.editError=''; this.http.put<Employee>(`/api/employees/${this.editing.id}/salary`,{salary:this.editSalary}).subscribe({next:()=>{this.saving=false;this.editing=null;this.refresh();},error:err=>{this.saving=false;this.editError=err?.error?.detail || 'Could not update salary. Please try again.';}}); }
}
