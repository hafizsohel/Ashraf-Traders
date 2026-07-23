package com.example.ashraftraders.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NavigationViewModel extends ViewModel {

    public enum NavTab {
        DASHBOARD,
        SALES
    }

    private final MutableLiveData<NavTab> selectedTab = new MutableLiveData<>(NavTab.DASHBOARD);
    private final MutableLiveData<Boolean> isRadialMenuOpen = new MutableLiveData<>(false);

    public LiveData<NavTab> getSelectedTab() {
        return selectedTab;
    }

    public LiveData<Boolean> isRadialMenuOpen() {
        return isRadialMenuOpen;
    }

    public void selectTab(NavTab tab) {
        selectedTab.setValue(tab);
        if (Boolean.TRUE.equals(isRadialMenuOpen.getValue())) {
            isRadialMenuOpen.setValue(false);
        }
    }

    public void toggleRadialMenu() {
        Boolean currentState = isRadialMenuOpen.getValue();
        isRadialMenuOpen.setValue(currentState == null || !currentState);
    }

    public void closeRadialMenu() {
        if (Boolean.TRUE.equals(isRadialMenuOpen.getValue())) {
            isRadialMenuOpen.setValue(false);
        }
    }
}