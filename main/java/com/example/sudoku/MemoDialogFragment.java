package com.example.sudoku;

// MemoDialogFragment.java (일부)

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import androidx.fragment.app.DialogFragment;

public class MemoDialogFragment extends DialogFragment {

    // 현재 클릭된 CustomButton 객체 (MainActivity에서 설정)
    private CustomButton targetButton;
    private ToggleButton[] toggleButtons = new ToggleButton[9];

    public static MemoDialogFragment newInstance(CustomButton button) {
        MemoDialogFragment fragment = new MemoDialogFragment();
        fragment.targetButton = button;
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_memo, null);

        // 1. ToggleButton 연결 및 초기 상태 설정
        for (int i = 1; i <= 9; i++) {
            int toggleId = getResources().getIdentifier("memo_" + i, "id", requireActivity().getPackageName());
            toggleButtons[i-1] = view.findViewById(toggleId);

            // CustomButton의 기존 메모 상태를 로드하여 ToggleButton에 반영
            if (targetButton != null) {
                // targetButton.getMemo()[i-1] 메서드가 CustomButton에 있어야 합니다.
                // 현재는 구현되지 않았으므로 임시로 생략
            }
        }

        // 2. 버튼 이벤트 리스너 설정
        view.findViewById(R.id.btn_ok).setOnClickListener(v -> handleOk());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> handleDelete());

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    // ... (핸들러 메서드 구현은 다음 단계에 상세히)

    private void handleOk() {
        if (targetButton == null) return;

        boolean[] selectedMemos = new boolean[9];
        for (int i = 0; i < 9; i++) {
            selectedMemos[i] = toggleButtons[i].isChecked(); // 선택된 상태 저장
        }

        // CustomButton에 선택된 메모 적용 (40페이지)
        targetButton.setMemos(selectedMemos);

        // 메모를 설정하면 충돌 검사 로직이 필요 없음 (숫자를 입력하지 않았으므로)

        dismiss(); // 다이얼로그 닫기
    }

    private void handleDelete() {
        if (targetButton == null) return;

        // CustomButton의 모든 메모 삭제 (40페이지)
        targetButton.clearMemos();

        dismiss(); // 다이얼로그 닫기
    }
}