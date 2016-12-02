package net.mavericklabs.mitra.listener;

import net.mavericklabs.mitra.ui.adapter.SubjectAndGradeFragmentListAdapter;

import java.util.List;

/**
 * Created by root on 13/11/16.
 */

public interface OnDialogFragmentDismissedListener {
    void onDialogFragmentDismissed(List<SubjectAndGradeFragmentListAdapter.SubjectAndGradeObject> objectList);
}