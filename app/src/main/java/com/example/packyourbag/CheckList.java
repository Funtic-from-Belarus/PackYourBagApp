package com.example.packyourbag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.packyourbag.Adapter.CheckListAdapter;
import com.example.packyourbag.Constants.MyConstants;
import com.example.packyourbag.Data.AppData;
import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CheckList extends AppCompatActivity {

    RecyclerView recyclerView;
    CheckListAdapter checkListAdapter;
    RoomDB database;
    List<Items> itemsList = new ArrayList<>();
    String header, show;

    EditText txtAdd;
    Button btnAdd;
    LinearLayout linearLayout;

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_1, menu);

        if(MyConstants.MY_SELECTIONS.equals(header)){
            menu.getItem(0).setVisible(false);
            menu.getItem(2).setVisible(false);
            menu.getItem(3).setVisible(false);
        } else if(MyConstants.MY_LIST_CAMEL_CASE.equals(header)){
            menu.getItem(1).setVisible(false);
        }

        MenuItem menuItem = menu.findItem(R.id.btnSearch);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Items> finalList = new ArrayList<>();
                for(Items item: itemsList){
                    if(item.getItemname().toLowerCase().startsWith(newText.toLowerCase())){
                        finalList.add(item);
                    }
                }

                updateRecycle(finalList);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent = new Intent(this, CheckList.class);

        AppData appData = new AppData(database, this);

        switch (item.getItemId()){
            case R.id.btnMySelection:
                intent.putExtra(MyConstants.HEADER_SMALL, MyConstants.MY_SELECTIONS);
                intent.putExtra(MyConstants.SHOW_SMALL, MyConstants.FALSE_STRING);
                startActivityForResult(intent, 101);
                return true;

            case R.id.btnCustomList:
                intent.putExtra(MyConstants.HEADER_SMALL, MyConstants.MY_LIST_CAMEL_CASE);
                intent.putExtra(MyConstants.SHOW_SMALL, MyConstants.TRUE_STRING);
                startActivity(intent);
                return true;

            case R.id.btnDeleteDefault:
                new AlertDialog.Builder(this)
                        .setTitle("Удалить данные по умолчанию")
                        .setMessage("Вы уверены? \n\n Так как это удалит данные предоставленные приложением при скачивании ")
                        .setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                appData.persistDataByCategory(header, true);
                                itemsList = database.mainDao().getAll(header);
                                updateRecycle(itemsList);
                            }
                        })
                        .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(R.drawable.baseline_warning_24)
                        .show();

                return true;


            case R.id.btnReset:
                new AlertDialog.Builder(this)
                        .setTitle("Установить записи по умолчанию")
                        .setMessage("Вы уверены?\n\nТак как это удалит все записи которые вы добавили самостоятельно в " + header + " и загрузит данные по умолчанию")
                        .setPositiveButton("Подтвердить", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                appData.persistDataByCategory(header, false);
                                itemsList = database.mainDao().getAll(header);
                                updateRecycle(itemsList);
                            }
                        })
                        .setNegativeButton("Закрыть", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .setIcon(R.drawable.baseline_warning_24)
                        .show();

                return true;


            case R.id.btnExit:
                    this.finishAffinity();
                Toast.makeText(this, "Вы вышли из приложения 'Собери свою сумку'", Toast.LENGTH_SHORT).show();
                return  true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            itemsList = database.mainDao().getAll(header);
            updateRecycle(itemsList);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        header = intent.getStringExtra(MyConstants.HEADER_SMALL);
        show = intent.getStringExtra(MyConstants.SHOW_SMALL);

        getSupportActionBar().setTitle(header);

        txtAdd = findViewById(R.id.txtAdd);
        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recycleView);
        linearLayout = findViewById(R.id.linearLayout);

        database = RoomDB.getInstance(this);

        if(MyConstants.FALSE_STRING.equals(show)){
            linearLayout.setVisibility(View.GONE);
            itemsList = database.mainDao().getAllSelected(true);

        } else {
            itemsList = database.mainDao().getAll(header);
        }

        updateRecycle(itemsList);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String itemName = txtAdd.getText().toString();
                if(itemName != null && !itemName.isEmpty()){
                    addNewItem(itemName);
                    Toast.makeText(CheckList.this, "Предмет добавлен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CheckList.this, "Пустое поле не может быть добавлено", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private  void addNewItem(String itemName){
        Items item = new Items();
        item.setChecked(false);
        item.setCategory(header);
        item.setItemname(itemName);
        item.setAddby(MyConstants.USER_SMALL);
        database.mainDao().saveItem(item);

        itemsList = database.mainDao().getAll(header);
        updateRecycle(itemsList);

        recyclerView.scrollToPosition(checkListAdapter.getItemCount() - 1);
        txtAdd.setText("");
    }

    private void updateRecycle(List<Items> itemsList){

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));

        checkListAdapter = new CheckListAdapter(CheckList.this, itemsList, database, show);

        recyclerView.setAdapter(checkListAdapter);

    }
}