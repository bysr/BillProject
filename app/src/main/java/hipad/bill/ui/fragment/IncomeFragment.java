package hipad.bill.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hipad.bill.MyApplication;
import hipad.bill.adapter.IncomeAdapter;
import hipad.bill.bean.AccountBook;
import hipad.bill.bean.AccountInfo;
import hipad.bill.bean.MonthAccout;
import hipad.bill.ui.AccountMonthAty;
import hipad.bill.ui.ColumnInfoActivity;
import hipad.bill.utils.DateUtils;
import hipad.billproject.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IncomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM = "param";


    // TODO: Rename and change types of parameters

    private int cYear, cMonth;
    private AccountBook book;
    private MonthAccout monthAccout;


    RecyclerView recycler_income;

    IncomeAdapter adapter;
    List<AccountInfo> list;

    private DbManager db;

    private OnFragmentInteractionListener mListener;

    private Map<Integer, List<AccountInfo>> map;//存储一类账单

    public IncomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IncomeFragment newInstance(int year, int month, AccountBook book) {
        IncomeFragment fragment = new IncomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, year);
        args.putInt(ARG_PARAM2, month);
        args.putSerializable(ARG_PARAM, book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cYear = getArguments().getInt(ARG_PARAM1, DateUtils.nowYear());
            cMonth = getArguments().getInt(ARG_PARAM2, DateUtils.nowMonth());
            book = (AccountBook) getArguments().getSerializable(ARG_PARAM);
        }
        AccountMonthAty infoAty = (AccountMonthAty) getActivity();
        infoAty.setIncomeClickedListener(new AccountMonthAty.OnButtonClickedListener() {
            @Override
            public void onClickedMonth(int year, int month) {
                cYear = year;
                cMonth = month;
                initData();


            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        recycler_income = (RecyclerView) getActivity().findViewById(R.id.recycler_income);
        list = new ArrayList<>();
        adapter = new IncomeAdapter(list, getActivity());
        adapter.setTitle("总收入");
        recycler_income.setAdapter(adapter);

        recycler_income.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recycler_income.setItemAnimator(new DefaultItemAnimator());


        setHeader(recycler_income);
        adapter.setOnItemClickListener(new IncomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, AccountInfo data) {
                Intent intent = new Intent(getActivity(), ColumnInfoActivity.class);
                intent.putExtra("year", cYear);
                intent.putExtra("month", cMonth);
                intent.putExtra("type", data.getType());
                intent.putExtra("book", book);
                startActivity(intent);

            }
        });

        db = MyApplication.getInstance().getDbManager();
        map = new HashMap<>();
    }

    private List<AccountInfo> generateData() {
        map.clear();
        List<AccountInfo> mlist = null;//数据库查询数据
        List<AccountInfo> Maplist = new ArrayList<>();//整理后的数据

        long start = DateUtils.dateToStamp(DateUtils.getFirstDayOfMonth(cYear, cMonth, null));
        long end = DateUtils.dateToStamp(DateUtils.getLastDayOfMonth(cYear, cMonth, null));
        try {
            mlist = book.getInfoPos(db, start, end);//这个是查询出所有账单，要求是查询出
        } catch (DbException e) {
            e.printStackTrace();
        }

        //对查询结果归类处理,逻辑简单点，通过map中转下list；

        for (int i = 0; i < mlist.size(); i++) {//
            int type = mlist.get(i).getType();//获取当前账单属性
            if (map.get(type) == null) {
                List<AccountInfo> list = new ArrayList<>();
                list.add(mlist.get(i));
                map.put(type, list);
            } else {
                List<AccountInfo> list = map.get(type);
                list.add(mlist.get(i));
            }
        }
        //
        for (List<AccountInfo> list : map.values()) {
            AccountInfo accountInfo = new AccountInfo();
            float count = 0f;
            for (AccountInfo info : list) {
                count += info.getBill_money();
                accountInfo.setType(info.getType());
            }
            accountInfo.setBill_money(count);
            Maplist.add(accountInfo);
        }


        return Maplist;
    }


    private void initData() {
        adapter.addDatas(generateData());
    }


    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_head_pchart, view, false);
        adapter.setHeaderView(header);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
