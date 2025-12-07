package com.example.sudoku;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;

public class CustomButton extends FrameLayout {
    int row;
    int col;
    int value;

    TextView textView;
    //    addView(textView);

    // 메모 기능 ! ! ! ! !
    private boolean[] memo = new boolean[9]; // 메모 저장 (1~9)
    private TableLayout memoLayout; // 메모 숫자를 표시할 3x3 TableLayout
    private TextView[] memoTextViews = new TextView[9]; // 메모 숫자 1~9에 해당하는 TextView 배열

    /**
     * @param selectedMemos 선택된 메모 배열 (true/false)
     */
    public void setMemos(boolean[] selectedMemos) {
        this.memo = selectedMemos;
        this.value = 0; // 메모를 설정하면 칸의 값은 0으로 초기화 (40페이지)
        textView.setText(""); // 큰 숫자 텍스트 숨김

        updateMemoDisplay(); // 메모 레이아웃을 업데이트합니다.
    }

    /**
     * 모든 메모를 삭제하고 메모 레이아웃을 숨깁니다.
     */
    public void clearMemos() {
        for (int i = 0; i < 9; i++) {
            memo[i] = false;
        }
        updateMemoDisplay();
    }

    /**
     * CustomButton에 저장된 메모 상태에 따라 메모 레이아웃의 TextView를 업데이트
     */
    private void updateMemoDisplay() {
        // 메모가 하나라도 있는지 확인
        boolean hasMemo = false;
        for (int n = 0; n < 9; n++) {
            if (memo[n]) {
                hasMemo = true;
                memoTextViews[n].setText(String.valueOf(n + 1));
            } else {
                memoTextViews[n].setText("");
            }
        }

        // 메모가 있을 때만 메모 레이아웃을 보이게 함
        if (hasMemo) {
            memoLayout.setVisibility(View.VISIBLE);
            textView.setVisibility(View.INVISIBLE); // 메모 중일 때 큰 숫자 숨김
        } else {
            memoLayout.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE); // 메모가 없으면 큰 숫자 보이게 함
        }
    }

    // 메모 기능 끝 ! ! ! !

    public CustomButton(Context context, int row, int col) {
        super(context);
        this.row = row;
        this.col = col;
        this.value = 0;

        init(context);

    }

    private void init(Context context) {
        // TextView를 생성하여 FrameLayout 내부에 추가하고 중앙에 배치
        textView = new TextView(context);

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        textView.setLayoutParams(params);

        addView(textView);

        // 메모 기능 추가 !
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        memoLayout = (TableLayout) layoutInflater.inflate(R.layout.layout_memo, this, false); // this를 부모로 설정
        memoLayout.setVisibility(View.INVISIBLE); // 초기에는 숨김
        // FrameLayout에 메모 레이아웃 추가 (겹쳐서 표시)
        addView(memoLayout);

        // 메모 TextView 찾기
        int k = 0;
        for (int i = 0; i < memoLayout.getChildCount(); i++) { // 4개 TableRow 순회
            View childView = memoLayout.getChildAt(i);
            if (childView instanceof TableRow) {
                TableRow tableRow = (TableRow) childView;
                for (int j = 0; j < tableRow.getChildCount(); j++, k++) { // 각 TableRow 내 TextView 순회
                    memoTextViews[k] = (TextView) tableRow.getChildAt(j); // TextView를 배열에 저장
                }
            }
        }

        // 클릭 가능하도록 설정
        setClickable(true);

        setBackgroundResource(R.drawable.button_selector);
    }

    public void set(int a) {
        this.value = a;
        if (a != 0) {
            clearMemos(); // 숫자가 입력되면 메모 삭제
            textView.setText(String.valueOf(a));
            memoLayout.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setText("");
            textView.setVisibility(View.VISIBLE);
        }
    }

    private boolean isConflicting = false;

    public void setConflict() {
        if (!isConflicting) {
            isConflicting = true;
            setBackgroundColor(0xFFFFCCCC);
        }
    }

    public void unsetConflict() {
        if (isConflicting) {
            isConflicting = false;

            setBackgroundResource(R.drawable.button_selector);
        }
    }

    public int getValue() {
        return this.value;
    }

    public boolean[] getMemo() {
        return memo;
    }

}