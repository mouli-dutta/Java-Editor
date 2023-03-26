package com.javaeditor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class CodeEditorActivity extends AppCompatActivity {
    private EditText title, code;
    private String beforeStr = "";

    private String userCodeId = null, userCodeTitle = null, userCode = null;

    private CodeDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_code_editor);

        db = new CodeDatabase(this);
        getAndSetIntentData();

        initToolbar();
        initTitle();
        initCode();
        initKeyboardHelper();

    }

    private void getAndSetIntentData() {
        if (getIntent().hasExtra("user_code_id") &&
                getIntent().hasExtra("user_code_title") &&
                getIntent().hasExtra("user_code")) {
            userCodeId = getIntent().getStringExtra("user_code_id");
            userCodeTitle = getIntent().getStringExtra("user_code_title");
            userCode = getIntent().getStringExtra("user_code");
        }
    }

    private void initKeyboardHelper() {
        View layoutKeyboard = findViewById(R.id.helper_keyboard);
        HelperKeyboard keyboard = new HelperKeyboard(layoutKeyboard, code);
        keyboard.initializeButtons();
    }

    private void initCode() {
        code = findViewById(R.id.code_edit_txt);
        code.addTextChangedListener(setHighlighter());
        if (userCode != null) code.setText(userCode);
    }

    private TextWatcher setHighlighter() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeStr = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s == null) return;
                code.removeTextChangedListener(this);
                try {

                    int initialCursorPos = start + before;
                    int numberOfCharsRightOfCursor = beforeStr.substring(initialCursorPos).length();

                    SyntaxHighlighter sh = new SyntaxHighlighter(CodeEditorActivity.this);
                    String original = s.toString();
                    SpannableString formatted = sh.setHighLight(original);

                    code.setText(formatted);
                    code.setSelection(getNewCursorPosition(numberOfCharsRightOfCursor, original));

                } catch (Exception e) {
                    Toast.makeText(CodeEditorActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                code.addTextChangedListener(this);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private int getNewCursorPosition(int numberOfCharsRightOfCursor, String original) {
        int position = 0;
        String reverse = new StringBuilder(original).reverse().toString();
        for (int i = 0; i < reverse.length(); i++) {
            if (numberOfCharsRightOfCursor == 0) break;
            else {
                numberOfCharsRightOfCursor--;
            }
            position++;
        }
        return original.length() - position;
    }

    private void initTitle() {
        title = findViewById(R.id.title_edit_txt);
        if (userCodeTitle != null) title.setText(userCodeTitle);
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_arrow_back_24);
        toolbar.setNavigationOnClickListener(v -> {
            startActivity(new Intent(CodeEditorActivity.this, MainActivity.class));
            finish();
        });
        toolbar.inflateMenu(R.menu.code_editor_menu);
        setMenuTextColor(toolbar.getMenu());
        Objects.requireNonNull(toolbar.getOverflowIcon()).setColorFilter(Color.WHITE , PorterDuff.Mode.SRC_ATOP);
        toolbar.setOnMenuItemClickListener(this::setMenuItemClickListener);
    }

    private void setMenuTextColor(Menu menu) {
        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString ss = new SpannableString(menu.getItem(i).getTitle().toString());
            ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.white)), 0, ss.length(), 0);
            item.setTitle(ss);
        }
    }

    private boolean setMenuItemClickListener(MenuItem item) {
        int id = item.getItemId();
        String titleStr = title.getText().toString();
        String codeStr = code.getText().toString();

        if (id == R.id.save_code) {
            if (userCodeId != null) {
                db.updateData(userCodeId, titleStr, codeStr);
            } else {
                db.addData(titleStr, codeStr);
            }

            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else if (id == R.id.copy_code) {
            copyToClipboard(codeStr);
        } else if (id == R.id.share_code){
            shareCode(codeStr);

        } else {
            Toast.makeText(this, "Wrong choice", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    private void copyToClipboard(String codeStr) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData cd = ClipData.newPlainText("copy", codeStr);
        cm.setPrimaryClip(cd);
        Toast.makeText(this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void shareCode(String codeStr) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, codeStr);
        intent.setType("text/plain");
        intent = Intent.createChooser(intent, "Share With");
        startActivity(intent);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(CodeEditorActivity.this, MainActivity.class));
        finish();
    }
}