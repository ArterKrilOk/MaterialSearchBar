package com.pixelswordgames.materialsearchbar;

import android.content.Context;
import android.content.res.TypedArray;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.pixelswordgames.materialsearch.R;

public class MaterialSearchView extends FrameLayout {
    private CardView cardView;
    private AppCompatEditText editText;
    private ImageView searchIcon, menuIcon;
    private RecyclerView searchResultView;

    private OnMenuItemClickListener onMenuItemClickListener;
    private OnTextChangeListener onTextChangeListener;
    private OnNavigationClickListener onNavigationClickListener;

    public MaterialSearchView(Context context) {
        super(context);
        init(null);
    }

    public MaterialSearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public MaterialSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public MaterialSearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        inflate(getContext(), R.layout.search_bar_view, this);

        cardView = findViewById(R.id.searchCard);
        editText = findViewById(R.id.searchText);
        searchIcon = findViewById(R.id.imageView);
        menuIcon = findViewById(R.id.menuView);
        searchResultView = findViewById(R.id.searchPreview);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(onTextChangeListener != null)
                    onTextChangeListener.onTextChange(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View view, int actionId, KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    if(onTextChangeListener != null)
                        onTextChangeListener.onTextSubmit(editText.getText().toString());
                    clearFocus();
                    return true;
                }
                return false;
            }
        });

        menuIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onMenuItemClickListener != null)
                    onMenuItemClickListener.onClick(view);
            }
        });
        searchIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onNavigationClickListener != null)
                    onNavigationClickListener.onClick(view);
            }
        });

        if(attrs != null)
            setAttrs(attrs);
    }
    private void setAttrs(AttributeSet attrs){
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.MaterialSearchView);
        String hint = attributes.getString(R.styleable.MaterialSearchView_hint);
        int menuIcon = attributes.getResourceId(R.styleable.MaterialSearchView_menuIcon, R.drawable.ic_baseline_filter_list_24);
        int navIcon = attributes.getResourceId(R.styleable.MaterialSearchView_navIcon, R.drawable.ic_baseline_search_24);

        if (hint == null || hint.isEmpty())
            hint = getContext().getString(android.R.string.search_go);

        attributes.recycle();

        searchIcon.setImageResource(navIcon);
        this.menuIcon.setImageResource(menuIcon);
        editText.setHint(hint);
    }

    public void setOnTextChangeListener(OnTextChangeListener onTextChangeListener) {
        this.onTextChangeListener = onTextChangeListener;
    }

    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        this.onMenuItemClickListener = onMenuItemClickListener;
    }

    public void setOnNavigationClickListener(OnNavigationClickListener onNavigationClickListener) {
        this.onNavigationClickListener = onNavigationClickListener;
    }

    public void setHint(CharSequence hint){
        editText.setHint(hint);
    }

    public void setAdapter(RecyclerView.Adapter adapter){
        searchResultView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        searchResultView.setLayoutManager(layoutManager);
    }

    @Override
    public void clearFocus() {
        searchResultView.setVisibility(View.GONE);
        editText.clearFocus();
        closeKeyboard();

        super.clearFocus();
    }

    public void showRecycler(){
        searchResultView.setVisibility(View.VISIBLE);
    }

    private void closeKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public interface OnTextChangeListener {
        void onTextChange(String query);
        void onTextSubmit(String query);
    }

    public interface OnMenuItemClickListener {
        void onClick(View view);
    }
    public interface OnNavigationClickListener {
        void onClick(View view);
    }
}
