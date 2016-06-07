package com.stfalcon.android.ui.fragments.profile;

import android.databinding.ObservableField;

import com.stfalcon.android.R;
import com.stfalcon.android.data.events.OpenFragmentEvent;
import com.stfalcon.android.data.models.entities.profile.Profile;
import com.stfalcon.android.data.models.responses.profile.ProfileResponse;
import com.stfalcon.android.data.preferences.Preferences;
import com.stfalcon.android.databinding.FragmentProfileBinding;
import com.stfalcon.android.network.rest.api.client.stfalconClient;
import com.stfalcon.android.network.rest.api.services.ProfileService;
import com.stfalcon.android.ui.base.binding.fragments.FragmentViewModel;
import com.stfalcon.android.ui.fragments.auth.AuthFragment;
import com.stfalcon.android.ui.fragments.profile.edit.ProfileEditFragment;
import com.stfalcon.android.ui.fragments.profile.friends.FriendsFragment;
import com.stfalcon.android.ui.fragments.profile.places.MyPlacesFragment;
import com.stfalcon.android.ui.fragments.profile.settings.SettingsFragment;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Response;

/**
 * Created by Anton Bevza on 4/29/16.
 */
public class ProfileFragmentVM extends FragmentViewModel<ProfileFragment, FragmentProfileBinding> {

    public ObservableField<Profile> profile = new ObservableField<>();

    public ProfileFragmentVM(ProfileFragment fragment, FragmentProfileBinding binding) {
        super(fragment, binding);
    }

    @Override
    protected void initialize(FragmentProfileBinding binding) {
        loadProfile();
    }

    private void loadProfile() {
        new stfalconClient(getActivity()).getService(ProfileService.class).getCurrent()
                .enqueue(getActivity(), this::onLoadProfile);
    }

    private void onLoadProfile(Response<ProfileResponse> response) {
        profile.set(response.body().getProfileBody());
    }

    public void openFriends() {
        EventBus.getDefault().post(
                new OpenFragmentEvent(FriendsFragment.newInstance(), true));
    }

    public void openPlaces() {
        EventBus.getDefault().post(
                new OpenFragmentEvent(MyPlacesFragment.newInstance(), true));
    }

    public void openSettings() {
        EventBus.getDefault().post(
                new OpenFragmentEvent(SettingsFragment.newInstance(), true));
    }

    public void logout() {
        Preferences.getManager().clear();
        EventBus.getDefault().post(new OpenFragmentEvent(AuthFragment.newInstance(), false));
    }

    public void openEdit() {
        EventBus.getDefault().post(new OpenFragmentEvent(ProfileEditFragment.newInstance(profile.get()), true));
    }
}
