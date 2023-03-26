package com.javaeditor;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HelperKeyboard {
    private final View view;
    private final EditText editText;

    public HelperKeyboard(View view, EditText editText) {
        this.view = view;
        this.editText = editText;
    }

    public void initializeButtons() {
        Button tabBtn = view.findViewById(R.id.tab_button);
        tabBtn.setOnClickListener(v -> updateText("    "));

        Button leftBrace = view.findViewById(R.id.left_curly_braces);
        leftBrace.setOnClickListener(v -> updateBraces("{}"));

        Button rightBrace = view.findViewById(R.id.rightCurlyBraces);
        rightBrace.setOnClickListener(v -> updateText("}"));

        Button semicolon = view.findViewById(R.id.semicolon);
        semicolon.setOnClickListener(v -> updateText(";"));

        Button equals = view.findViewById(R.id.equals_btn);
        equals.setOnClickListener(v -> updateText("="));

        Button leftParentheses = view.findViewById(R.id.left_parentheses);
        leftParentheses.setOnClickListener(v -> updateBraces("()"));

        Button rightParentheses = view.findViewById(R.id.right_parentheses);
        rightParentheses.setOnClickListener(v -> updateText(")"));

        Button leftSquareBracket = view.findViewById(R.id.left_square_bracket);
        leftSquareBracket.setOnClickListener(v -> updateBraces("[]"));

        Button rightSquareBracket = view.findViewById(R.id.right_square_bracket);
        rightSquareBracket.setOnClickListener(v -> updateText("]"));

        Button leftAngleBracket = view.findViewById(R.id.left_angle_bracket);
        leftAngleBracket.setOnClickListener(v -> updateBraces("<>"));

        Button rightAngleBracket = view.findViewById(R.id.right_angle_bracket);
        rightAngleBracket.setOnClickListener(v -> updateText(">"));

        Button apostrophy = view.findViewById(R.id.apostrophy);
        apostrophy.setOnClickListener(v -> updateBraces("\"\""));

        Button singleApostrophy = view.findViewById(R.id.single_apostrophy);
        singleApostrophy.setOnClickListener(v -> updateBraces("''"));

        Button plus = view.findViewById(R.id.plus);
        plus.setOnClickListener(v -> updateText("+"));

        Button minus = view.findViewById(R.id.minus);
        minus.setOnClickListener(v -> updateText("-"));

        Button multiply = view.findViewById(R.id.multiply);
        multiply.setOnClickListener(v -> updateText("*"));

        Button divide = view.findViewById(R.id.diivde);
        divide.setOnClickListener(v -> updateText("/"));

        Button remainder= view.findViewById(R.id.remainder);
        remainder.setOnClickListener(v -> updateText("%"));

        Button exponential = view.findViewById(R.id.exponential);
        exponential.setOnClickListener(v -> updateText("^"));

        Button period = view.findViewById(R.id.period);
        period.setOnClickListener(v -> updateText("."));

        Button comma = view.findViewById(R.id.comma);
        comma.setOnClickListener(v -> updateText(","));

        Button quotes = view.findViewById(R.id.quote);
        quotes.setOnClickListener(v -> updateBraces("``"));

        Button tilde = view.findViewById(R.id.tilde);
        tilde.setOnClickListener(v -> updateText("~"));

        Button question = view.findViewById(R.id.question);
        question.setOnClickListener(v -> updateText("?"));

        Button exclamation = view.findViewById(R.id.exclamation);
        exclamation.setOnClickListener(v -> updateText("!"));

        Button ampersand = view.findViewById(R.id.ampersand);
        ampersand.setOnClickListener(v -> updateText("&"));

        Button colon = view.findViewById(R.id.colon);
        colon.setOnClickListener(v -> updateText(":"));

        Button hash = view.findViewById(R.id.hash);
        hash.setOnClickListener(v -> updateText("#"));

        Button at = view.findViewById(R.id.at);
        at.setOnClickListener(v -> updateText("@"));

        Button verticalBar = view.findViewById(R.id.vertical_bar);
        verticalBar.setOnClickListener(v -> updateText("|"));

        Button multilineComment = view.findViewById(R.id.comment_multi);
        multilineComment.setOnClickListener(v -> updateBraces("/**/"));

        Button dollar = view.findViewById(R.id.dollar);
        dollar.setOnClickListener(v -> updateText("$"));

        Button lowerHyphen = view.findViewById(R.id.lower_hyphen);
        lowerHyphen.setOnClickListener(v -> updateText("_"));

        Button backslash = view.findViewById(R.id.backslash);
        backslash.setOnClickListener(v -> updateText("\\"));

    }

    private void updateText(String strToAdd) {
        String oldStr = editText.getText().toString();
        int cursorPos = editText.getSelectionStart();

        String leftString = oldStr.substring(0, cursorPos);
        String rightString = oldStr.substring(cursorPos);

        editText.setText(String.format("%s%s%s", leftString, strToAdd, rightString));
        editText.setSelection(cursorPos + strToAdd.length());
    }

    private void updateBraces(String strToAdd) {
        String oldStr = editText.getText().toString();
        int cursorPos = editText.getSelectionStart();

        String leftString = oldStr.substring(0, cursorPos);
        String rightString = oldStr.substring(cursorPos);

        editText.setText(String.format("%s%s%s", leftString, strToAdd, rightString));
        editText.setSelection(cursorPos + strToAdd.length()/2);
    }
}
