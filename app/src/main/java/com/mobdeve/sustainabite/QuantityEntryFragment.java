package com.mobdeve.sustainabite;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobdeve.sustainabite.databinding.FragmentQuantityInputEntryBinding;

public class QuantityEntryFragment extends Fragment {

    private FragmentQuantityInputEntryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentQuantityInputEntryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the dropdown menu
        String[] quantityOptions = getResources().getStringArray(R.array.quantity);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.quantity_dropdown, quantityOptions);
        binding.autoCompleteTextView.setAdapter(adapter);


        AutoCompleteTextView autoCompleteTextView = binding.autoCompleteTextView;
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnClickListener(v -> autoCompleteTextView.showDropDown());
    }

    //added this to get the data from the fragment.

    public String getSelectedQuantity(){
        return binding.autoCompleteTextView.getText().toString();
    }

    //this is to set the selected quantity and gather it from the database.
    public void setSelectedQuantity(String quantityType){
        if (binding != null && quantityType != null){
            binding.autoCompleteTextView.setText(quantityType, false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

