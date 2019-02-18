package hipad.bill.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import hipad.bill.adapter.EditClassifyAdapter;
import hipad.bill.bean.AccountInfo;
import hipad.bill.constant.SortUtils;
import hipad.bill.ui.EditAccountActivity;
import hipad.billproject.R;

import static hipad.bill.constant.SortUtils.PAY;
import static hipad.billproject.R.id.img_photo;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditInFragment.OnFragmentIncome} interface
 * to handle interaction events.
 * Use the {@link EditInFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditInFragment extends BaseFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private AccountInfo mParam1;


    RecyclerView recycle_classify;
    EditClassifyAdapter adapter;

    List<SortUtils> list;


    private SortUtils utils = PAY;//设置工资为默认选项

    private OnFragmentIncome mListener;


    public EditInFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment IncomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditInFragment newInstance(AccountInfo param1) {
        EditInFragment fragment = new EditInFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = (AccountInfo) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_account1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    private void initView() {
        recycle_classify = getView().findViewById(R.id.recycle_classify);
        if (mParam1 != null) {
            SortUtils tools = SortUtils.get(mParam1.getType());//获取tools
            if (tools.getType() != 2) {
                utils = tools;
            }

        }


        list = new ArrayList<>();

        adapter = new EditClassifyAdapter(list, getActivity());
        recycle_classify.setAdapter(adapter);

        recycle_classify.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recycle_classify.setItemAnimator(new DefaultItemAnimator());
        adapter.addDatas(generateData());
        adapter.setOnItemClickListener(new EditClassifyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                setEntity(position);

            }
        });


    }

    private void setEntity(int position) {
        utils.setSelect(false);//修改之前选中值的状态
        //设置图片
        SortUtils sort = list.get(position);
        sort.setSelect(true);

        //回调到上一个页面
        onButtonPressed(null,sort.getCode());


        adapter.notifyDataSetChanged();
        utils = list.get(position);
    }

    private List<SortUtils> generateData() {

        List<SortUtils> list = new ArrayList<>();
        for (SortUtils c : SortUtils.values()) {
            if (c.getType() == 1 || c.getType() == 0) {
                if (utils.getCode() == c.getCode()) {
                    c.setSelect(true);
                    utils = c;
                } else c.setSelect(false);
                list.add(c);
            }
        }
        return list;

    }


    private void initEvent() {
        EditAccountActivity infoAty = (EditAccountActivity) getActivity();
        infoAty.setClickedIncomeListener(new EditAccountActivity.OnButtonClickedListener() {
            @Override
            public void updateFunction() {
          //模拟点击事件
                setEntity(0);
            }
        });

    }

    private void initData() {
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(AccountInfo info, int type) {
        if (mListener != null) {
            mListener.onFragmentIncomeAct(info, type);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentIncome) {
            mListener = (OnFragmentIncome) context;
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

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case img_photo:


                break;

        }

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
    public interface OnFragmentIncome {
        // TODO: Update argument type and name
        void onFragmentIncomeAct(AccountInfo info, int type);
    }
}
