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

import static hipad.bill.constant.SortUtils.EAT;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditOutFragment.OnFragmentExpend} interface
 * to handle interaction events.
 * Use the {@link EditOutFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditOutFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private AccountInfo mParam1;

    RecyclerView recycle_classify;
    EditClassifyAdapter adapter;


    List<SortUtils> list;

    private SortUtils utils = EAT;//设置工资为默认选项


    private OnFragmentExpend mListener;


    public EditOutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ExpendFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EditOutFragment newInstance(AccountInfo param1) {
        EditOutFragment fragment = new EditOutFragment();
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
        return inflater.inflate(R.layout.fragment_edit_account2, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
    }

    private void initEvent() {
        EditAccountActivity infoAty = (EditAccountActivity) getActivity();
        infoAty.setClickedExpendListener(new EditAccountActivity.OnButtonClickedListener() {
            @Override
            public void updateFunction() {
                setEntity(0);
            }

        });


    }


    private void initView() {

        recycle_classify = getView().findViewById(R.id.recycle_classify);

        if (mParam1 != null) {
            SortUtils tools = SortUtils.get(mParam1.getType());//获取tools
            if (tools.getType() != 1) {
                utils = tools;
            }
        }

        list = new ArrayList<>();

        adapter = new EditClassifyAdapter(list, getActivity());
        recycle_classify.setAdapter(adapter);

        recycle_classify.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        recycle_classify.setItemAnimator(new DefaultItemAnimator());
        adapter.addDatas(generateData());
//        setHeader(recycle_classify);
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

        onButtonPressed(null, sort.getCode());
        adapter.notifyDataSetChanged();
        utils = list.get(position);
    }

    private List<SortUtils> generateData() {
        List<SortUtils> list = new ArrayList<>();
        for (SortUtils c : SortUtils.values()) {
            if (c.getType() == 2 || c.getType() == 0) {
                if (utils.getCode() == c.getCode()) {
                    c.setSelect(true);
                    utils = c;
                } else c.setSelect(false);
                list.add(c);
            }
        }
        return list;

    }


    private void setHeader(RecyclerView view) {
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.item_head_pchart, view, false);
        adapter.setHeaderView(header);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(AccountInfo info, int Type) {
        if (mListener != null) {
            mListener.onFragmentExpendAct(info, Type);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentExpend) {
            mListener = (OnFragmentExpend) context;
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
    public interface OnFragmentExpend {
        // TODO: Update argument type and name
        void onFragmentExpendAct(AccountInfo info, int Type);
    }
}
