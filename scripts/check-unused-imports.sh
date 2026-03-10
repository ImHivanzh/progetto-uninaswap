#!/bin/bash
# Check for potentially unused imports in Java files

REPORT_FILE="scripts/unused-imports-report.txt"
SRC_DIR="src"

echo "=== Unused Imports Analysis ===" > "$REPORT_FILE"
echo "Generated: $(date)" >> "$REPORT_FILE"
echo "" >> "$REPORT_FILE"

echo "Scanning for unused imports..."

find "$SRC_DIR" -name "*.java" -type f | while read -r file; do
    # Get all imports from the file
    grep "^import " "$file" 2>/dev/null | while read -r import_line; do
        # Extract the class name (last part after the last dot)
        class_name=$(echo "$import_line" | sed 's/.*\.\([^.;]*\);/\1/')

        # Skip wildcard imports
        if echo "$import_line" | grep -q "\*"; then
            continue
        fi

        # Count occurrences of the class name in the file (excluding the import line itself)
        usage_count=$(grep -c "\b$class_name\b" "$file" 2>/dev/null || echo 0)

        # If only 1 occurrence (the import itself), it's likely unused
        if [ "$usage_count" -le 1 ]; then
            echo "POTENTIALLY UNUSED: $file" >> "$REPORT_FILE"
            echo "  Import: $import_line" >> "$REPORT_FILE"
            echo "  Class: $class_name" >> "$REPORT_FILE"
            echo "" >> "$REPORT_FILE"
        fi
    done
done

unused_count=$(grep -c "^POTENTIALLY UNUSED:" "$REPORT_FILE" 2>/dev/null || echo 0)
echo "" >> "$REPORT_FILE"
echo "=== Analysis Complete ===" >> "$REPORT_FILE"
echo "Total potentially unused imports: $unused_count" >> "$REPORT_FILE"

echo "Analysis complete. Found $unused_count potentially unused imports"
cat "$REPORT_FILE"
