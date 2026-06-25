package healthnet.utils;

/**
 * Console Colors and Formatting Utilities for the HealthNet CLI.
 * Provides ANSI escape codes, styled output helpers, box-drawing,
 * and a startup banner for a premium terminal experience.
 */
public class ConsoleColors {

    // ─── ANSI Escape Codes ────────────────────────────────────────────────
    public static final String RESET     = "\033[0m";
    public static final String BOLD      = "\033[1m";
    public static final String DIM       = "\033[2m";
    public static final String ITALIC    = "\033[3m";
    public static final String UNDERLINE = "\033[4m";

    // Foreground Colors
    public static final String RED       = "\033[31m";
    public static final String GREEN     = "\033[32m";
    public static final String YELLOW    = "\033[33m";
    public static final String BLUE      = "\033[34m";
    public static final String MAGENTA   = "\033[35m";
    public static final String CYAN      = "\033[36m";
    public static final String WHITE     = "\033[37m";

    // Bright Foreground Colors
    public static final String BRIGHT_RED     = "\033[91m";
    public static final String BRIGHT_GREEN   = "\033[92m";
    public static final String BRIGHT_YELLOW  = "\033[93m";
    public static final String BRIGHT_CYAN    = "\033[96m";
    public static final String BRIGHT_WHITE   = "\033[97m";

    // Background Colors
    public static final String BG_RED    = "\033[41m";
    public static final String BG_GREEN  = "\033[42m";
    public static final String BG_BLUE   = "\033[44m";
    public static final String BG_CYAN   = "\033[46m";
    public static final String BG_WHITE  = "\033[47m";

    // ─── Inline Color Wrappers ────────────────────────────────────────────
    public static String success(String msg)   { return GREEN + msg + RESET; }
    public static String error(String msg)     { return RED + msg + RESET; }
    public static String warning(String msg)   { return YELLOW + msg + RESET; }
    public static String info(String msg)      { return CYAN + msg + RESET; }
    public static String highlight(String msg) { return BOLD + MAGENTA + msg + RESET; }
    public static String header(String msg)    { return BOLD + CYAN + msg + RESET; }

    // ─── Section Formatting ───────────────────────────────────────────────

    /**
     * Prints a major section header with ═══ borders and centered title.
     */
    public static void printHeader(String title) {
        System.out.println();
        System.out.println(BOLD + CYAN + "  " + repeat('=', 63) + RESET);
        int padding = Math.max(1, (61 - title.length()) / 2);
        int rightPad = Math.max(1, 61 - padding - title.length());
        System.out.println(BOLD + WHITE + "  " + " " + repeat(' ', padding) + title + repeat(' ', rightPad) + RESET);
        System.out.println(BOLD + CYAN + "  " + repeat('=', 63) + RESET);
    }

    /**
     * Prints a sub-section header with >>> marker.
     */
    public static void printSubHeader(String title) {
        System.out.println();
        System.out.println(BOLD + YELLOW + "  >>> " + title + RESET);
        printDivider();
    }

    /**
     * Prints a thin divider line.
     */
    public static void printDivider() {
        System.out.println(DIM + "  " + repeat('-', 63) + RESET);
    }

    /**
     * Prints a double-line divider.
     */
    public static void printDoubleDivider() {
        System.out.println(CYAN + "  " + repeat('=', 63) + RESET);
    }

    // ─── Menu Item Formatting ─────────────────────────────────────────────

    /**
     * Prints a numbered menu item.
     */
    public static void printMenuItem(String num, String text) {
        System.out.println(CYAN + "  [" + BOLD + WHITE + num + RESET + CYAN + "]  " + WHITE + text + RESET);
    }

    /**
     * Prints a numbered menu item with a DSA tag in parentheses.
     */
    public static void printMenuItemWithTag(String num, String text, String tag) {
        if (tag == null || tag.isEmpty()) {
            printMenuItem(num, text);
        } else {
            System.out.println(CYAN + "  [" + BOLD + WHITE + num + RESET + CYAN + "]  "
                    + WHITE + text + DIM + "  (" + tag + ")" + RESET);
        }
    }

    // ─── Status Messages ──────────────────────────────────────────────────

    /** Prints a green success message with checkmark. */
    public static void printSuccess(String text) {
        System.out.println(GREEN + "  [OK] " + text + RESET);
    }

    /** Prints a red error message with X mark. */
    public static void printError(String text) {
        System.out.println(RED + "  [ERROR] " + text + RESET);
    }

    /** Prints a yellow warning message. */
    public static void printWarning(String text) {
        System.out.println(YELLOW + "  [!] " + text + RESET);
    }

    /** Prints a cyan info message. */
    public static void printInfo(String text) {
        System.out.println(CYAN + "  [i] " + text + RESET);
    }

    /** Prints a labeled result line (label: value). */
    public static void printResult(String label, String value) {
        System.out.println(WHITE + "    " + label + ": " + BOLD + GREEN + value + RESET);
    }

    // ─── Input Prompt ─────────────────────────────────────────────────────

    /** Prints a styled input prompt (no newline). */
    public static void printPrompt(String text) {
        System.out.print(YELLOW + "  >> " + WHITE + text + RESET);
    }

    // ─── Loading Animation ────────────────────────────────────────────────

    /** Prints a loading message with animated dots. */
    public static void printLoading(String message) {
        System.out.print(DIM + "  " + message);
        try {
            for (int i = 0; i < 3; i++) {
                Thread.sleep(150);
                System.out.print(".");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(" Done!" + RESET);
    }

    // ─── Table Formatting ─────────────────────────────────────────────────

    /** Prints a formatted table header row. */
    public static void printTableHeader(String... columns) {
        StringBuilder sb = new StringBuilder();
        sb.append(BOLD).append(CYAN).append("    ");
        for (String col : columns) {
            sb.append(String.format("%-20s", col));
        }
        sb.append(RESET);
        System.out.println(sb.toString());
        System.out.println(DIM + "    " + repeat('-', columns.length * 20) + RESET);
    }

    /** Prints a formatted table data row. */
    public static void printTableRow(String... values) {
        StringBuilder sb = new StringBuilder();
        sb.append(WHITE).append("    ");
        for (String val : values) {
            sb.append(String.format("%-20s", val));
        }
        sb.append(RESET);
        System.out.println(sb.toString());
    }

    // ─── ASCII Art Banner ─────────────────────────────────────────────────

    /** Prints the HealthNet startup banner. */
    public static void printBanner() {
        System.out.println();
        System.out.println(BOLD + CYAN   + "    ================================================================" + RESET);
        System.out.println(BOLD + CYAN   + "    ||                                                            ||" + RESET);
        System.out.println(BOLD + GREEN  + "    ||    ##   ##  ######   ####   ##    ######  ##   ##           ||" + RESET);
        System.out.println(BOLD + GREEN  + "    ||    ##   ##  ##      ##  ##  ##      ##    ##   ##           ||" + RESET);
        System.out.println(BOLD + GREEN  + "    ||    #######  ####    ######  ##      ##    #######           ||" + RESET);
        System.out.println(BOLD + GREEN  + "    ||    ##   ##  ##      ##  ##  ##      ##    ##   ##           ||" + RESET);
        System.out.println(BOLD + GREEN  + "    ||    ##   ##  ######  ##  ##  ######  ##    ##   ##           ||" + RESET);
        System.out.println(BOLD + CYAN   + "    ||                                                            ||" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "    ||              ##   ##  ######  ######                      ||" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "    ||              ###  ##  ##        ##                        ||" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "    ||              ## # ##  ####      ##                        ||" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "    ||              ##  ###  ##        ##                        ||" + RESET);
        System.out.println(BOLD + BRIGHT_WHITE + "    ||              ##   ##  ######    ##                        ||" + RESET);
        System.out.println(BOLD + CYAN   + "    ||                                                            ||" + RESET);
        System.out.println(BOLD + RED    + "    ||                     ___+___                                ||" + RESET);
        System.out.println(BOLD + RED    + "    ||                    |       |                               ||" + RESET);
        System.out.println(BOLD + RED    + "    ||                 ___+___+___+___                            ||" + RESET);
        System.out.println(BOLD + RED    + "    ||                    |       |                               ||" + RESET);
        System.out.println(BOLD + RED    + "    ||                    |___+___|                               ||" + RESET);
        System.out.println(BOLD + CYAN   + "    ||                                                            ||" + RESET);
        System.out.println(BOLD + BRIGHT_YELLOW + "    ||     Intelligent Hospital Resource Management System       ||" + RESET);
        System.out.println(BOLD + CYAN   + "    ||                                                            ||" + RESET);
        System.out.println(           DIM + "    ||      DSA Project  *  Java 17  *  CLI Application           ||" + RESET);
        System.out.println(           DIM + "    ||   BST | AVL | B-Tree | Graphs | DP | Sorting | Greedy     ||" + RESET);
        System.out.println(BOLD + CYAN   + "    ||                                                            ||" + RESET);
        System.out.println(BOLD + CYAN   + "    ================================================================" + RESET);
        System.out.println();
    }

    // ─── Utility ──────────────────────────────────────────────────────────

    private static String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }
}
