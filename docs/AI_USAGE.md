# AI-assisted development notes

This assessment encourages intentional use of agentic AI tools. Keep this file honest: update it with the actual prompts/tools used while you build and verify the project. The examples below are suggested prompts, not a claim that a particular tool executed them.

## Example prompts to document
- “Turn the assessment brief into a one-page requirements document. Separate explicit requirements from assumptions and proposed features.”
- “Review the API design for 10,000 employee records. Recommend pagination, indexes and safe salary summary semantics.”
- “Write focused tests for salary validation, missing employees and controller response behavior.”
- “Review this diff for correctness, security, maintainability and SQLite compatibility. Do not rewrite unrelated code.”
- “Give me a manual acceptance-test checklist for search, filtering, pagination and salary updates.”

## Human review checklist
- Read and understand each generated class before committing it.
- Run the test suite and record the real result.
- Inspect SQL and confirm queries are parameterized.
- Verify that the UI handles loading and API errors.
- Update this document with the actual prompts and review decisions.
