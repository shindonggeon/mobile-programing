package com.example.sudoku;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;



public class MainActivity extends AppCompatActivity {
    public void onClickNum(View v) {
        if (clickedCustomButton == null) return;

        // tag 속성을 통해 클릭된 버튼에서 숫자 값을 가져옴
        String tag = (String) v.getTag();
        int number = Integer.parseInt(tag);

        // CustomButton의 set() 함수를 사용하여 숫자 변경
        clickedCustomButton.set(number);

        // 숫자패드 숨기기
        numpadLayout.setVisibility(View.INVISIBLE);


        checkAllConflicts();
    }

    /**
     * CANCEL 버튼 클릭 시 호출
     */
    public void onClickCancel(View v) {
        // 숫자 패드 닫음
        numpadLayout.setVisibility(View.INVISIBLE);
    }

    /**
     * DEL 버튼 클릭 시 호출
     */
    public void onClickDel(View v) {
        if (clickedCustomButton == null) return;

        // CustomButton의 숫자 삭제
        clickedCustomButton.set(0);

        // 숫자 패드 닫음
        numpadLayout.setVisibility(View.INVISIBLE);

        checkAllConflicts();
    }

    private void checkAllConflicts() {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                buttons[r][c].unsetConflict();
            }
        }

        // 새로운 충돌 검사 및 설정
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                int value = buttons[i][j].getValue();
                if (value != 0) { // 숫자가 입력된 칸만 검사

                    // 가로, 세로, 3x3 블록 충돌을 모두 확인
                    boolean hasRowConflict = checkRowConflict(i, j, value);
                    boolean hasColConflict = checkColConflict(i, j, value);
                    boolean hasBlockConflict = checkBlockConflict(i, j, value);

                    // 현재 칸이 충돌을 발생시키면, 충돌된 모든 칸을 붉게
                    if (hasRowConflict || hasColConflict || hasBlockConflict) {
                        highlightConflictingCells(i, j, value);
                    }
                }
            }
        }
    }

    private void highlightConflictingCells(int r, int c, int value) {
        buttons[r][c].setConflict();

        // 가로줄 충돌
        for (int j = 0; j < 9; j++) {
            // 현재 칸이 아닌데 같은 값을 가지면 충돌
            if (j != c && buttons[r][j].getValue() == value) {
                buttons[r][j].setConflict();
            }
        }

        // 세로줄 충돌
        for (int i = 0; i < 9; i++) {
            if (i != r && buttons[i][c].getValue() == value) {
                buttons[i][c].setConflict();
            }
        }

        // 3x3 블록 충돌
        int startRow = r / 3 * 3;
        int startCol = c / 3 * 3;
        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i == r && j == c) continue; // 현재 칸 제외
                if (buttons[i][j].getValue() == value) {
                    buttons[i][j].setConflict();
                }
            }
        }
    }

    private boolean checkRowConflict(int r, int c, int value) {
        for (int j = 0; j < 9; j++) {
            if (j != c && buttons[r][j].getValue() == value) {
                return true; // 가로 충돌 발생
            }
        }
        return false;
    }

    private boolean checkColConflict(int r, int c, int value) {
        for (int i = 0; i < 9; i++) {
            if (i != r && buttons[i][c].getValue() == value) {
                return true; // 세로 충돌 발생
            }
        }
        return false;
    }

    private boolean checkBlockConflict(int r, int c, int value) {
        int startRow = r / 3 * 3;
        int startCol = c / 3 * 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i == r && j == c) continue; // 현재 칸 제외
                if (buttons[i][j].getValue() == value) {
                    return true; // 3x3 블록 충돌 발생
                }
            }
        }
        return false;
    }

    /// ////////////////////////////////////////////////////////////////

    private TableLayout table;
    private CustomButton[][] buttons = new CustomButton[9][9];
    private TableLayout numpadLayout; // 숫자 패드 레이아웃
    private CustomButton clickedCustomButton; // 현재 클릭된 버튼 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BoardGenerator board = new BoardGenerator();

        table = (TableLayout) findViewById(R.id.tableLayout);
        numpadLayout = (TableLayout) findViewById(R.id.numpadLayout);

        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this); //tableRow 생성

            TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    0,            // 높이를 0dp로 설정
                    1.0f);



            int rowBottomMargin = (i == 2 || i == 5) ? 8 : 0; //3번째 6번째 행마다 간격
            layoutParams.setMargins(0, 0, 0, rowBottomMargin);
            tableRow.setLayoutParams(layoutParams);

            for (int j = 0; j < 9; j++) {
                buttons[i][j] = new CustomButton(this, i, j); // i행에 button을 생성

                TableRow.LayoutParams buttonParams = new TableRow.LayoutParams(
                        0,                                    // 너비를 0dp로 설정
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f);

                int rightMargin = 2;
                int bottomMargin = 2;

                if (j == 2 || j == 5) {
                    rightMargin = 18; // 오른쪽 마진을 굵게 설정
                }
                if (i == 2 || i == 5) {
                    bottomMargin = 10; // 아래쪽 마진을 굵게 설정 (선택적)
                }
                buttonParams.setMargins(2, 2, rightMargin, bottomMargin);
                buttons[i][j].setLayoutParams(buttonParams);
                tableRow.addView(buttons[i][j]); // 생성된 버튼 i행에 추가

                int number = board.get(i, j);
                if(Math.random() < 0.3) {
                    buttons[i][j].set(0);
                }
                else {
                    buttons[i][j].set(number);
                    buttons[i][j].setClickable(false);
                }

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // 클릭된 CustomButton 객체를 저장
                        clickedCustomButton = (CustomButton) view;

                        // 숫자 패드 보이기 (22페이지)
                        numpadLayout.setVisibility(View.VISIBLE); // [cite: 355]
                        // numpadLayout.setVisivility(VISIBLE); [cite: 404]

                    }
                });
                // 롱클릭 리스너 추가 (32페이지)
                buttons[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        // 롱클릭한 CustomButton 객체를 저장
                        CustomButton clickedButton = (CustomButton) view;

                        // 메모 입력 다이얼로그 표시 (30페이지)
                        MemoDialogFragment dialog = MemoDialogFragment.newInstance(clickedButton);
                        // FragmentManager를 사용하여 DialogFragment를 띄웁니다.
                        dialog.show(getSupportFragmentManager(), "MemoDialog");

                        return true;
                    }
                });

//                tableRow.addView(buttons[i][j]);
            }

            table.addView(tableRow); //table에 완성된 tableRow추가
        }
    }
}