#!/bin/bash
# Dead Code Analysis Script for UninaSwap
# Identifies methods with zero references in the codebase

set -euo pipefail

REPORT_FILE="scripts/dead-code-report.txt"
SRC_DIR="src"

echo "=== UninaSwap Dead Code Analysis ===" > "$REPORT_FILE"
echo "Generated: $(date)" >> "$REPORT_FILE"
echo "" >> "$REPORT_FILE"

# Patterns to exclude (framework methods, entry points)
EXCLUDE_PATTERNS=(
    "main\("
    "actionPerformed\("
    "mouseClicked\("
    "mousePressed\("
    "mouseReleased\("
    "mouseEntered\("
    "mouseExited\("
    "windowOpened\("
    "windowClosing\("
    "windowClosed\("
    "windowIconified\("
    "windowDeiconified\("
    "windowActivated\("
    "windowDeactivated\("
    "run\("
    "get\("
    "set\("
    "is\("
)

# Extract all method definitions
echo "Scanning for method definitions..."
find "$SRC_DIR" -name "*.java" -type f | while read -r file; do
    # Extract methods: public/private/protected ... methodName(...)
    grep -n "^\s*\(public\|private\|protected\|static\)\s.*\s\+\w\+\s*(" "$file" | while IFS=: read -r line_num method_line; do
        # Extract method name
        method_name=$(echo "$method_line" | sed -E 's/.*\s+([a-zA-Z_][a-zA-Z0-9_]*)\s*\(.*/\1/')

        # Skip constructors (method name == class name)
        class_name=$(basename "$file" .java)
        if [ "$method_name" = "$class_name" ]; then
            continue
        fi

        # Skip excluded patterns
        skip=false
        for pattern in "${EXCLUDE_PATTERNS[@]}"; do
            if echo "$method_line" | grep -q "$pattern"; then
                skip=true
                break
            fi
        done

        if [ "$skip" = true ]; then
            continue
        fi

        # Count references (excluding the definition line)
        ref_count=$(grep -r "\b$method_name\s*(" "$SRC_DIR" --include="*.java" | grep -v "^$file:$line_num:" | wc -l | tr -d ' ')

        # If zero references, add to report
        if [ "$ref_count" -eq 0 ]; then
            echo "UNUSED: $file:$line_num - $method_name" >> "$REPORT_FILE"
            echo "  Line: $method_line" >> "$REPORT_FILE"
            echo "" >> "$REPORT_FILE"
        fi
    done
done

echo "" >> "$REPORT_FILE"
echo "=== Analysis Complete ===" >> "$REPORT_FILE"
echo "Total unused methods: $(grep -c "^UNUSED:" "$REPORT_FILE" || echo 0)" >> "$REPORT_FILE"

echo "Analysis complete. Report saved to: $REPORT_FILE"
cat "$REPORT_FILE"
