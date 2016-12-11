package net.mavericklabs.mitra.listener;

import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.ui.adapter.SubjectAndGradeFragmentListAdapter;

import java.util.List;

/**
 * Created by amoghpalnitkar on 13/11/16.
 */

public interface OnDialogFragmentDismissedListener {
    void onDialogFragmentDismissed(List<BaseObject> objectList, int sourceFragment);
}