package com.example.packyourbag.Data;

import static com.example.packyourbag.Constants.MyConstants.PERSONAL_CARE_CAMEL_CASE;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.example.packyourbag.Constants.MyConstants;
import com.example.packyourbag.Database.RoomDB;
import com.example.packyourbag.Models.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppData extends Application {

    RoomDB database;
    String category;
    Context context;

    public static final String LAST_VERSION = "LAST_VERSION";
    public static final int NEW_VERSION = 1;

    public AppData(RoomDB database) {
        this.database = database;
    }

    public AppData(RoomDB database, Context context) {
        this.database = database;
        this.context = context;
    }

    public List<Items> getBasicData(){
        String[] data = {"Виза", "Паспорт", "Билеты", "Кошелек",
                "Водительские права", "Ключи от дома", "Книга", "Подушка", "Зонтик"};

        return prepareItemList(MyConstants.BASIC_NEEDS_CAMEL_CASE, data);
    }

    public List<Items> getPersonalCareData(){
        String[] data = {"Зубная щетка", "Зубная паста", "Зубная нить", "Жидкость для полоскания рта",
                "Пена для бритья", "Лезвие для бритья", "Мыло", "Шампунь", "Кондиционер для волос",
                "Щетка", "Расческа", "Помада для губ", "Контактные линзы", "Духи", "Диски",
                "Дезодорант", "Для макияжа", "Влажные салфетки", "Ножницы для ногтей", "Палочки для ушей"
        };

        return prepareItemList(MyConstants.PERSONAL_CARE_CAMEL_CASE, data);
    }

    public List<Items> getNeedsData(){
        String[] data = {"Рюкзак", "Ежедневная сумка", "Мешок для стирки", "Набор для шитья",
                "Дорожный замок", "Багаж", "Журналы", "Спортивное оборудование", "Важные номера"};

        return prepareItemList(MyConstants.NEEDS_CAMEL_CASE, data);
    }

    public List<Items> getCarSuppliesData(){
        String[] data = {"Запасной ключ от машины", "Насос", "домкрат", "авто холодильник",
                "автомобильный чехол", "автомобильное зарядное устройство", "солнцезащитная шторка"};

        return prepareItemList(MyConstants.CAR_SUPPLIES_CAMEL_CASE, data);
    }

    public List<Items> getBeachSuppliesData(){
        String[] data = {"Очки для воды", "Матрас", "Солнезащитный крем", "Пляжный зонтик",
                "Костюм для плаванья", "Пакет для пляжа", "Полотенце",
                "Водонепроницаемые часы", "Лежак", "Тапки для пляжа"};

        return prepareItemList(MyConstants.BEACH_SUPPLIES_CAMEL_CASE, data);
    }

    public List<Items> getClothingData(){
        String[] data = {"Пижама", "Нижние белье", "Футболки", "Повседневные джинсы",
                "Вечерняя одежда", "Юбка", "Кардиган", "Шорты", "Куртка", "Штаны",
                "Брюки", "Костюм", "Пальто", "Дождевич",
                "Перчатки", "Шляпа", "Шарф",
                "Ремень", "Кроссовки", "Спортивные кроссовки"};

        return prepareItemList(MyConstants.CLOTHING_CAMEL_CASE, data);
    }

    public List<Items> getBabyNeedsData(){
        String[] data = {"Костюм для сна ребенку", "Костюм для ребенка", "Носки для ребенка", "Шляпа для ребенка",
                "Пижама для ребенка", "Полотенце для ребенка", "Одеяло для ребенка", "Бутылка для ребенка",
                "Термос еды для ребенка", "Влажные салфетки",
                };

        return prepareItemList(MyConstants.BABY_NEEDS_CAMEL_CASE, data);
    }

    public List<Items> getHealthData(){
        String[] data = {"Аспирин", "Витамины", "Пластырь", "Жидкость для линз",
                "Набор для линз", "Обезболивающее", "Презервативы",
                "Противовирусный", "Используемые наркотики"};

        return prepareItemList(MyConstants.HEALTH_CAMEL_CASE, data);
    }

    public List<Items> getTeechnologyData(){
        String[] data = {"Мобильный телефон", "Чехол для телефона", "Планшет для чтения", "Камера",
                "Зарядка для камеры", "Колонка", "Планшет", "Наушники", "Ноутбук", "Зарядка для ноутбука",
                "Мышка", "Pover Bank"};

        return prepareItemList(MyConstants.TECHNOLOGY_CAMEL_CASE, data);
    }

    public List<Items> getFoodData(){
        String[] data = {"Закуски", "Бутерброды", "Сок", "Чай",
                "Кофе", "Вода", "Чипсы", "Еда для ребенка"
        };

        return prepareItemList(MyConstants.FOOD_CAMEL_CASE, data);
    }



    public List<Items> prepareItemList(String category, String[] data){
        List<String> list = Arrays.asList(data);
        List<Items> dataList = new ArrayList<>();
        dataList.clear();

        for (int i = 0; i < list.size(); i++){
            dataList.add(new Items(list.get(i), category, false));
        }

        return dataList;
    }

    public List<List<Items>> getAlData(){
        List<List<Items>> listOfAllData = new ArrayList<>();
        listOfAllData.clear();
        listOfAllData.add(getFoodData());
        listOfAllData.add(getBabyNeedsData());
        listOfAllData.add(getTeechnologyData());
        listOfAllData.add(getBasicData());
        listOfAllData.add(getHealthData());
        listOfAllData.add(getClothingData());
        listOfAllData.add(getBeachSuppliesData());
        listOfAllData.add(getCarSuppliesData());
        listOfAllData.add(getPersonalCareData());
        listOfAllData.add(getNeedsData());

        return listOfAllData;
    }

    public void persistAllData(){
        List<List<Items>> listOfAllData = getAlData();

        for(List<Items> list : listOfAllData){
            for(Items items: list){
                database.mainDao().saveItem(items);
            }
        }
        System.out.println("Data added.");
    }

    public void persistDataByCategory(String category, Boolean onlyDelete){
        try{
        List<Items> list = deleteAndGetListByCategory(category, onlyDelete);
        if(!onlyDelete){
            for (Items item : list) {
                database.mainDao().saveItem(item);
                //Toast.makeText(context, category + " установлено успешно", Toast.LENGTH_SHORT).show();
            }
        }else {
           // Toast.makeText(context, category + " установлено успешно", Toast.LENGTH_SHORT).show();
        }
        } catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show();
        }
    }


    private List<Items> deleteAndGetListByCategory(String category, Boolean onlyDelete){
        if(onlyDelete){
            database.mainDao().deleteAllByCategoryAndAddedBy(category, MyConstants.SYSTEM_SMALL);
        } else {
            database.mainDao().deleteAllByCategory(category);
        }

        switch (category){
            case MyConstants.BASIC_NEEDS_CAMEL_CASE:
                return getBasicData();

            case MyConstants.CLOTHING_CAMEL_CASE:
                return getClothingData();

            case MyConstants.PERSONAL_CARE_CAMEL_CASE:
                return getPersonalCareData();

            case MyConstants.BEACH_SUPPLIES_CAMEL_CASE:
                return getBeachSuppliesData();

            case MyConstants.CAR_SUPPLIES_CAMEL_CASE:
                return getCarSuppliesData();

            case MyConstants.FOOD_CAMEL_CASE:
                return getFoodData();

            case MyConstants.HEALTH_CAMEL_CASE:
                return getHealthData();

            case MyConstants.TECHNOLOGY_CAMEL_CASE:
                return getTeechnologyData();

            case MyConstants.BABY_NEEDS_CAMEL_CASE:
                return getBabyNeedsData();

            case MyConstants.NEEDS_CAMEL_CASE:
                return getNeedsData();

            default:
                return new ArrayList<>();
        }

    }


}
