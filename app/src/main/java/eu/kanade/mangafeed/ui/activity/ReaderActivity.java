package eu.kanade.mangafeed.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import eu.kanade.mangafeed.R;
import eu.kanade.mangafeed.data.models.Page;
import eu.kanade.mangafeed.presenter.ReaderPresenter;
import eu.kanade.mangafeed.ui.adapter.ReaderPageAdapter;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(ReaderPresenter.class)
public class ReaderActivity extends BaseActivity<ReaderPresenter> {

    @Bind(R.id.view_pager) ViewPager viewPager;
    @Bind(R.id.page_number) TextView pageNumber;

    private ReaderPageAdapter adapter;
    private int currentPage;

    public static Intent newInstance(Context context) {
        return new Intent(context, ReaderActivity.class);
    }

    @Override
    public void onCreate(Bundle savedState) {
        super.onCreate(savedState);
        setContentView(R.layout.activity_reader);
        ButterKnife.bind(this);

        createAdapter();
        setupViewPager();
    }

    private void createAdapter() {
        adapter = new ReaderPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }

    private void setupViewPager() {
        viewPager.setOffscreenPageLimit(3);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                updatePageNumber();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onDestroy() {
        getPresenter().setCurrentPage(currentPage);
        super.onDestroy();
    }

    public void onPageList(List<Page> pages) {
        adapter.setPages(pages);
        updatePageNumber();
    }

    public void onPageDownloaded(Page page) {
        adapter.replacePage(page.getPageNumber(), page);
    }

    public void setCurrentPage(int position) {
        viewPager.setCurrentItem(position);
    }

    private void updatePageNumber() {
        String page = (currentPage+1) + "/" + adapter.getCount();
        pageNumber.setText(page);
    }
}