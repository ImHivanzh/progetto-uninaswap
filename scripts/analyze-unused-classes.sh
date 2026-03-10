#!/bin/bash
# Dead Code Analysis Script for UninaSwap - Classes and Enums
# Identifies unused classes and enums in the codebase

REPORT_FILE="scripts/unused-classes-report.txt"
SRC_DIR="src"

echo "=== UninaSwap Unused Classes/Enums Analysis ===" > "$REPORT_FILE"
echo "Generated: $(date)" >> "$REPORT_FILE"
echo "" >> "$REPORT_FILE"

echo "Scanning for class and enum definitions..."

# Find all class definitions
find "$SRC_DIR" -name "*.java" -type f | while read -r file; do
    # Extract class name from file
    class_name=$(basename "$file" .java)

    # Skip if it's the main App class
    if [ "$class_name" = "App" ]; then
        continue
    fi

    # Check if it's an enum
    is_enum=$(grep -E "^\s*public\s+enum\s+$class_name" "$file" 2>/dev/null)

    # Count references to this class (excluding the file itself and imports)
    ref_count=$(grep -r "\b$class_name\b" "$SRC_DIR" --include="*.java" 2>/dev/null | \
                grep -v "^$file:" | \
                grep -v "^import " | \
                wc -l | tr -d ' ')

    # If zero references, report it
    if [ "$ref_count" -eq 0 ]; then
        if [ -n "$is_enum" ]; then
            echo "UNUSED ENUM: $file - $class_name" >> "$REPORT_FILE"
        else
            echo "UNUSED CLASS: $file - $class_name" >> "$REPORT_FILE"
        fi
        echo "  References: $ref_count" >> "$REPORT_FILE"
        echo "" >> "$REPORT_FILE"
    fi
done

unused_count=$(grep -cE "^UNUSED (CLASS|ENUM):" "$REPORT_FILE" 2>/dev/null || echo 0)
echo "" >> "$REPORT_FILE"
echo "=== Analysis Complete ===" >> "$REPORT_FILE"
echo "Total unused classes/enums: $unused_count" >> "$REPORT_FILE"

echo "Analysis complete. Found $unused_count unused classes/enums"
cat "$REPORT_FILE"
