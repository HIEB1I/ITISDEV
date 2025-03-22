package com.mobdeve.sustainabite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.fragment.app.Fragment;
import com.mobdeve.sustainabite.databinding.InputEntryBinding;

public class InputEntryFragment extends Fragment {

    private InputEntryBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = InputEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        // Set up the dropdown menu
        String[] storageOptions = getResources().getStringArray(R.array.storage);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.storage_dropdown, storageOptions);

        AutoCompleteTextView autoCompleteTextView = binding.autoCompleteTextView;
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());

        return view;


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
