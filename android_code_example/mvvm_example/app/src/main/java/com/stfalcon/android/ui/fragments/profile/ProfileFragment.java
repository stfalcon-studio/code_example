package com.stfalcon.android.ui.fragments.profile;


import com.stfalcon.android.BR;
import com.stfalcon.android.R;
import com.stfalcon.android.databinding.FragmentProfileBinding;
import com.stfalcon.android.ui.base.binding.fragments.BindingFragment;

/**
 * Created by Anton Bevza on 5/4/16.
 */
public class ProfileFragment extends BindingFragment<ProfileFragmentVM, FragmentProfileBinding> {


    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }


    @Override
    protected ProfileFragmentVM onCreateViewModel(FragmentProfileBinding binding) {
        return new ProfileFragmentVM(this, getBinding());
    }

    @Override
    public int getVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutResources() {
        return R.layout.fragment_profile;
    }
}
