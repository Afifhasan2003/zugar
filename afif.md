# Zugar Backend & Database Specification

## Database Schema (SQLite)

```sql
CREATE TABLE IF NOT EXISTS users (
    id TEXT PRIMARY KEY,
    username TEXT UNIQUE NOT NULL,
    created_at INTEGER DEFAULT (UNIXEPOCH())
);

CREATE TABLE IF NOT EXISTS project_files (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    filename TEXT NOT NULL,
    content TEXT NOT NULL,
    updated_at INTEGER DEFAULT (UNIXEPOCH()),
    FOREIGN KEY(user_id) REFERENCES users(id)
);
```

## Query Examples

### Fetch Latest Documents
```sql
SELECT filename, updated_at 
FROM project_files 
WHERE user_id = ? 
ORDER BY updated_at DESC 
LIMIT 10;
```

## Implementation Tasks
- [x] Configure SQLite connection pool
- [x] Set up parameterized queries & security filters
- [ ] Implement JWT token authentication handler