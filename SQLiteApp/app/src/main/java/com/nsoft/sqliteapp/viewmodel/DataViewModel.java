package com.nsoft.sqliteapp.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nsoft.sqliteapp.model.ExpenseModel;
import com.nsoft.sqliteapp.repository.DataRepository;

import java.util.List;

public class DataViewModel extends AndroidViewModel {

    private final DataRepository dataRepository;

    public DataViewModel(@NonNull Application application) {
        super(application);
        dataRepository = DataRepository.getInstance(application);
    }

    public void addIncome(double amount, String reason) {
        dataRepository.addIncome(amount, reason);
    }

    public void addExpense(double amount, String reason) {
        dataRepository.addExpense(amount, reason);
    }

    public LiveData<List<ExpenseModel>> getAllData() {
        return dataRepository.getAllData();
    }

    public LiveData<List<ExpenseModel>> getIndividualData(String type) {
        return dataRepository.getIndividualData(type);
    }

    public LiveData<Double> getTotalIncome() {
        return dataRepository.getTotalIncome();
    }

    public LiveData<Double> getTotalExpense() {
        return dataRepository.getTotalExpense();
    }

    public LiveData<List<ExpenseModel>> getFilteredData(String type, String keyword) {
        return dataRepository.getFilteredData(type, keyword);
    }

}
