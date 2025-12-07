package com.example.sudoku;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;
import androidx.fragment.app.DialogFragment;

public class MemoDialogFragment extends DialogFragment {

    // 현재 클릭된 CustomButton 객체
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

        // ToggleButton 연결 및 초기 상태 설정
        for (int i = 1; i <= 9; i++) {
            int toggleId = getResources().getIdentifier("memo_" + i, "id", requireActivity().getPackageName());
            toggleButtons[i-1] = view.findViewById(toggleId);

            // CustomButton의 기존 메모 상태를 로드하여 ToggleButton에 반영
            if (targetButton != null) {
                boolean[] currentMemos = targetButton.getMemo();

                // 🌟 가져온 상태로 ToggleButton의 isChecked 상태를 설정합니다.
                toggleButtons[i-1].setChecked(currentMemos[i-1]);
            }
        }

        // 버튼 이벤트 리스너 설정
        view.findViewById(R.id.btn_ok).setOnClickListener(v -> handleOk());
        view.findViewById(R.id.btn_cancel).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.btn_delete).setOnClickListener(v -> handleDelete());

        return new AlertDialog.Builder(requireContext())
                .setView(view)
                .create();
    }

    private void handleOk() {
        if (targetButton == null) return;

        boolean[] selectedMemos = new boolean[9];
        for (int i = 0; i < 9; i++) {
            selectedMemos[i] = toggleButtons[i].isChecked(); // 선택된 상태 저장
        }

        // CustomButton에 선택된 메모 적용
        targetButton.setMemos(selectedMemos);

        dismiss();
    }

    private void handleDelete() {
        if (targetButton == null) return;

        // CustomButton의 모든 메모 삭제
        targetButton.clearMemos();

        dismiss();
    }
}