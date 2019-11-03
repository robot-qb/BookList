package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.casper.testdrivendevelopment.data.BookFragmentAdapter;
import com.casper.testdrivendevelopment.data.BookListFragment;
import com.casper.testdrivendevelopment.data.BookSaver;
import com.casper.testdrivendevelopment.data.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_UPDATE=CONTEXT_MENU_DELETE+1;
    public static final int CONTEXT_MENU_ADDNEW = CONTEXT_MENU_UPDATE+1;
    public static final int CONTEXT_MENU_ABOUT = CONTEXT_MENU_ADDNEW+1;
    public static final int REQUEST_ADDNEW_CODE = 901;
    public static final int REQUEST_UPDATE_CODE = 902;

    private ArrayList<Book> theBooks;

    BookSaver bookSaver;

    private BooksArrayAdapter theAdapter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bookSaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        bookSaver=new BookSaver(this);
        theBooks=bookSaver.load();
        if(theBooks.size()==0)
            InitData();

        theAdapter=new BooksArrayAdapter(this,R.layout.linearlayout,theBooks);

        BookFragmentAdapter myPageAdapter = new BookFragmentAdapter(getSupportFragmentManager());

        ArrayList<Fragment> datas = new ArrayList<Fragment>();
        datas.add(new BookListFragment(theAdapter));
        datas.add(new WebViewFragment());
        datas.add(new MapViewFragment());

        myPageAdapter.setData(datas);

        ArrayList<String> titles = new ArrayList<String>();
        titles.add("图书");
        titles.add("新闻");
        titles.add("卖家");

        myPageAdapter.setTitles(titles);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        
// 将适配器设置进ViewPager
        viewPager.setAdapter(myPageAdapter);
// 将ViewPager与TabLayout相关联
        tabLayout.setupWithViewPager(viewPager);



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==findViewById(R.id.list_view_books)){
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(theBooks.get(info.position).getTitle());//info.position是获取这个item的id
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0,CONTEXT_MENU_UPDATE,0,"更新");
            menu.add(0, CONTEXT_MENU_ADDNEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case REQUEST_ADDNEW_CODE:
                if(resultCode==RESULT_OK){
                    int insertPosition= data.getIntExtra("position",0);
                    String bookTitle=data.getStringExtra("book_title");
                    double bookPrice=data.getDoubleExtra("book_price",0);
                    theBooks.add(insertPosition,new Book(bookTitle,bookPrice,R.drawable.a4));
                    theAdapter.notifyDataSetChanged();

                    Toast.makeText(this,"新建成功",Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_UPDATE_CODE:
                if(resultCode==RESULT_OK){
                    int position=data.getIntExtra("position",0);
                    String name=data.getStringExtra("book_title");
                    double price =data.getDoubleExtra("book_price",0);

                    Book book=theBooks.get(position);
                    book.setTitle(name);
                    book.setPrice(price);
                    theAdapter.notifyDataSetChanged();

                    Toast.makeText(this, "更新成功", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case CONTEXT_MENU_DELETE :
                AdapterView.AdapterContextMenuInfo info=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                final int itemPosition=info.position;
                new android.app.AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("你确定要删除这条吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                theBooks.remove(itemPosition);
                                theAdapter.notifyDataSetChanged();
                                Toast.makeText(BookListMainActivity.this, "删除成功！", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create().show();
                break;
            case CONTEXT_MENU_UPDATE:
                AdapterView.AdapterContextMenuInfo menuinfo=(AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                Book book=theBooks.get(menuinfo.position);
                Intent intent2=new Intent(BookListMainActivity.this,NewBookActivity.class);
                intent2.putExtra("position",menuinfo.position);
                intent2.putExtra("book_title",book.getTitle());
                intent2.putExtra("book_price",book.getPrice());
                startActivityForResult(intent2, REQUEST_UPDATE_CODE);
                break;
            case CONTEXT_MENU_ADDNEW:
                final int insertPosition=((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                Intent intent=new Intent(BookListMainActivity.this,NewBookActivity.class);
                intent.putExtra("position",insertPosition);
                startActivityForResult(intent, REQUEST_ADDNEW_CODE);

                break;
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(BookListMainActivity.this,"图书列表",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void InitData() {

        theBooks.add(new Book("软件项目管理案例教程（第4版）",1, R.drawable.a1));
        theBooks.add(new Book("创新工程实践",1, R.drawable.a2));
        theBooks.add(new Book("信息安全数学基础（第2版）",1, R.drawable.a3));
    }

    public ArrayList<Book> getListBooks(){
        return theBooks;
    }

    public class BooksArrayAdapter extends ArrayAdapter<Book> {
        private int resourceId;
        public BooksArrayAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
            resourceId=resource;
        }
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater mInflater = LayoutInflater.from(this.getContext());
            View item = mInflater.inflate(this.resourceId, null);

            ImageView img=(ImageView)item.findViewById(R.id.image_view_book_cover);
            TextView name=(TextView)item.findViewById(R.id.text_view_book_title);
            TextView price=(TextView)item.findViewById(R.id.text_view_book_price);

            Book book_item= (Book) this.getItem(position);
            img.setImageResource(book_item.getCoverResourceId());
            name.setText(book_item.getTitle());
            price.setText(book_item.getPrice()+"");

            return item;

        }
    }
}
