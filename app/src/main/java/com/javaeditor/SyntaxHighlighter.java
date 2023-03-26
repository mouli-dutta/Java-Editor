package com.javaeditor;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyntaxHighlighter {
    private final Context context;

    public SyntaxHighlighter(Context context) {
        this.context = context;
    }

    public SpannableString setHighLight(String input) {
        SpannableString ss = new SpannableString(input);

        setKeyWordHighlight(input, ss);
        setClassHighLight(input, ss);
        setDigitHighlight(input, ss);
        setStringHighLight(input, ss);
        setCommentHighLight(input, ss);

        return ss;
    }

    private void setKeyWordHighlight(String input, SpannableString ss) {
        // regex for keywords
        String[] keyWords = {
                "abstract", "boolean", "break", "byte", "case", "catch", "char",
                "class", "continue", "default", "do", "double", "else", "enum", "extends",
                "final", "finally", "float", "for", "if", "implements", "import", "instanceof",
                "int", "interface", "long", "native", "new", "null", "package", "private",
                "protected", "public", "return", "short", "static", "strictfp", "super",
                "switch", "synchronized", "this", "throw", "throws", "transient", "try",
                "var", "void", "volatile", "while", "true", "false"
        };
        String regex = "\\b(?:" + join(keyWords) + ")\\b";
        Matcher m = Pattern.compile(regex).matcher(input);
        List<Integer> startPos = new ArrayList<>();
        List<Integer> endPos = new ArrayList<>();
        while (m.find()) {
            startPos.add(m.start());
            endPos.add(m.end());
        }

        setSpan(startPos, endPos, ss, R.color.syntax_purple);
    }

    private void setClassHighLight(String s, SpannableString ss) {
        // regex for class
        String regexClass = "\\b\\p{Lu}\\w+\\b";
        Matcher m2 = Pattern.compile(regexClass).matcher(s);
        List<Integer> startPosClass = new ArrayList<>();
        List<Integer> endPosClass = new ArrayList<>();
        while (m2.find()) {
            startPosClass.add(m2.start());
            endPosClass.add(m2.end());
        }
        setSpan(startPosClass, endPosClass, ss, R.color.syntax_blue);
    }

    private void setDigitHighlight(String s, SpannableString ss) {
        // regex for digits
        String regexDigits = "\\b(\\d+(([eE]|\\.)\\d*)?)|(([eE]|\\.)\\d+)\\b";
        Matcher m5 = Pattern.compile(regexDigits).matcher(s);
        List<Integer> startDigit = new ArrayList<>();
        List<Integer> endDigit = new ArrayList<>();
        while (m5.find()) {
            startDigit.add(m5.start());
            endDigit.add(m5.end());
        }
        setSpan(startDigit, endDigit, ss, R.color.syntax_yellow);
    }

    private void setStringHighLight(String s, SpannableString ss) {
        // regex for string and char literals
        String regexStr = "\"((?:\\\\.|[^\"\\\\])*)\"|'(?:\\\\.|.)'";
        Matcher m1 = Pattern.compile(regexStr).matcher(s);

        List<Integer> startPosString = new ArrayList<>();
        List<Integer> endPosString = new ArrayList<>();
        while (m1.find()) {
            startPosString.add(m1.start());
            endPosString.add(m1.end());
        }

        setSpan(startPosString, endPosString, ss, R.color.syntax_green);
    }

    private void setCommentHighLight(String s, SpannableString ss) {
        // regex for comments
        String regexComments = "//.*|(?s)/\\*.*?\\*/";
        Matcher m3 = Pattern.compile(regexComments).matcher(s);
        List<Integer> startComment = new ArrayList<>();
        List<Integer> endComment = new ArrayList<>();
        while (m3.find()) {
            startComment.add(m3.start());
            endComment.add(m3.end());
        }

        setItalicSpan(startComment, endComment, ss);
    }

    private void setSpan(@NotNull List<Integer> startPos, List<Integer> endPos, SpannableString spanned, int color) {
        for (int i = 0; i < startPos.size(); i++) {
            spanned.setSpan(new ForegroundColorSpan(
                            context.getResources().getColor(color)), startPos.get(i), endPos.get(i),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void setItalicSpan(@NotNull List<Integer> startPos, List<Integer> endPos, SpannableString spanned) {
        int syntax_comment = R.color.syntax_comment;

        for (int i = 0; i < startPos.size(); i++) {
            spanned.setSpan(new ForegroundColorSpan(
                            context.getResources().getColor(syntax_comment)), startPos.get(i), endPos.get(i),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            // make comment text italic
            spanned.setSpan(new StyleSpan(Typeface.ITALIC),
                    startPos.get(i), endPos.get(i), 0);
        }
    }

    @NotNull
    private String join(@NotNull String[] array) {
        StringBuilder sb = new StringBuilder();
        String delimiter = "";
        for (String s : array) {
            sb.append(delimiter);
            sb.append(s);
            delimiter = "|";
        }
        return sb.toString();
    }
}
