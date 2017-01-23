/*
 *
 *  * ************************************************************************
 *  *
 *  *  MAVERICK LABS CONFIDENTIAL
 *  *  __________________
 *  *
 *  *   [2015] Maverick Labs
 *  *   All Rights Reserved.
 *  *
 *  *  NOTICE:  All information contained herein is, and remains
 *  *  the property of Maverick Labs and its suppliers,
 *  *  if any.  The intellectual and technical concepts contained
 *  *  herein are proprietary to Maverick Labs
 *  *  and its suppliers and may be covered by U.S. and Foreign Patents,
 *  *  patents in process, and are protected by trade secret or copyright law.
 *  *  Dissemination of this information or reproduction of this material
 *  *  is strictly forbidden unless prior written permission is obtained
 *  *  from Maverick Labs.
 *  * /
 *
 */

package net.mavericklabs.mitra.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.mavericklabs.mitra.R;
import net.mavericklabs.mitra.model.api.BaseModel;
import net.mavericklabs.mitra.listener.OnChipRemovedListener;
import net.mavericklabs.mitra.model.BaseObject;
import net.mavericklabs.mitra.model.CommonCode;
import net.mavericklabs.mitra.model.Content;
import net.mavericklabs.mitra.ui.adapter.ChipLayoutAdapter;
import net.mavericklabs.mitra.ui.adapter.ContentVerticalCardListAdapter;
import net.mavericklabs.mitra.utils.CommonCodeUtils;
import net.mavericklabs.mitra.utils.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

import static android.view.View.GONE;

/**
 * Created by vishakha on 14/12/16.
 */

public class BaseContentFragment extends Fragment {
    @BindView(R.id.filter_recycler_view)
    RecyclerView filterRecyclerView;

    @BindView(R.id.view_below_filter_list)
    View viewBelowFilterList;

    @BindView(R.id.content_recycler_view)
    RecyclerView contentRecyclerView;

    @BindView(R.id.error_view)
    TextView errorView;

    @BindView(R.id.loading_panel)
    RelativeLayout loadingPanel;

//        @BindView(R.id.load_more)
//        Button loadMore;


    protected List<BaseObject> filterList;
    protected ChipLayoutAdapter filterAdapter;
    ContentVerticalCardListAdapter adapter;

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Logger.d("fragment -  on permission result");
        adapter.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    protected void setupFilterView(OnChipRemovedListener listener) {
        filterList = new ArrayList<>();
        filterAdapter = new ChipLayoutAdapter(filterList);
        filterAdapter.setShowRemoveButton(true);
        filterAdapter.setListener(listener);

        filterRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL,false));
        filterRecyclerView.setVisibility(GONE);
    }

    protected void removeFromFilterList(int position) {
        filterList.remove(position);
        filterAdapter.notifyItemRemoved(position);
        if(filterList.isEmpty()) {
            viewBelowFilterList.setVisibility(GONE);
        }
    }

    protected void addItemToFilterList(CommonCode commonCode) {
        filterRecyclerView.setVisibility(View.VISIBLE);
        viewBelowFilterList.setVisibility(View.VISIBLE);
        filterList.add(new BaseObject(commonCode, true));
        filterAdapter.setObjects(filterList);
        filterRecyclerView.swapAdapter(filterAdapter, false);
    }

    protected boolean isNextPageToBeLoaded(int newState, RecyclerView recyclerView) {
        if(newState == RecyclerView.SCROLL_STATE_IDLE) {
            Logger.d(" scrolled idle");
            LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
            int childCount = contentRecyclerView.getAdapter().getItemCount();

            if(lastVisibleItem == childCount - 1) {
                return true;
            }
        }

        return false;
    }

    protected void loadContent(Response<BaseModel<Content>> response,
                               int pageNumber, RecyclerView.OnScrollListener listener) {
        contentRecyclerView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        contentRecyclerView.clearOnScrollListeners();
        Logger.d(" Succes");
        if(pageNumber > 0) {
            adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
            adapter.stopLoading();
        }
        if(response.body().getData() != null) {
            List<Content> contents = response.body().getData();
            Logger.d(" contents " + contents.size());

            if(pageNumber == 0) {
                Logger.d(" in here ");
                if(contents.size() == 0) {
                    String error = CommonCodeUtils.getObjectFromCode(response.body().getResponseMessage()).getCodeNameForCurrentLocale();
                    Logger.d(" error " + error);
                    contentRecyclerView.setVisibility(View.GONE);
                    errorView.setVisibility(View.VISIBLE);
                    errorView.setText(error);
                } else {
                    if(contentRecyclerView.getAdapter() == null) {
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        contentRecyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new ContentVerticalCardListAdapter(getContext(), contents, BaseContentFragment.this);
                        contentRecyclerView.setAdapter(adapter);
                    } else {
                        adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                        adapter.setContents(contents);
                        adapter.notifyDataSetChanged();

                    }

                    //if current page number is 0 and there are contents available, only then set scroll listener
                    contentRecyclerView.addOnScrollListener(listener);
                }


            } else {
                adapter = (ContentVerticalCardListAdapter) contentRecyclerView.getAdapter();
                List<Content> originalContents = adapter.getContents();
                Logger.d(" original contents " + originalContents.size());
                originalContents.addAll(contents);
                adapter.setContents(originalContents);
                adapter.notifyDataSetChanged();
            }

        }
    }
}
