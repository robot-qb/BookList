package com.casper.testdrivendevelopment;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.ArrayList;
import java.util.List;

public class BookListMainActivity extends AppCompatActivity {

    public static final int CONTEXT_MENU_DELETE = 1;
    public static final int CONTEXT_MENU_ADDNEW = 2;
    public static final int CONTEXT_MENU_ABOUT = 3;
    private ArrayList<Book> theBooks;
    private ListView list_view_books;
    private BooksArrayAdapter theAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list_main);

        InitData();
        list_view_books=(ListView)findViewById(R.id.list_view_books);
        theAdapter=new BooksArrayAdapter(this,R.layout.linearlayout,theBooks);
        list_view_books.setAdapter(theAdapter);
        this.registerForContextMenu(list_view_books);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==list_view_books) {
            //获取适配器
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            //设置标题
            menu.setHeaderTitle(theBooks.get(info.position).getTitle());//info.position是获取这个item的id
            //设置内容 参数1为分组，参数2对应条目的id，参数3是指排列顺序，默认排列即可
            menu.add(0, CONTEXT_MENU_DELETE, 0, "删除");
            menu.add(0, CONTEXT_MENU_ADDNEW, 0, "新建");
            menu.add(0, CONTEXT_MENU_ABOUT, 0, "关于...");
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
                
            case CONTEXT_MENU_ADDNEW:
                final int insertPosition=((AdapterView.AdapterContextMenuInfo)item.getMenuInfo()).position;
                theBooks.add(insertPosition,new Book("无名书籍", R.drawable.book_2));
                theAdapter.notifyDataSetChanged();
                Toast.makeText(BookListMainActivity.this,"新建成功",Toast.LENGTH_LONG).show();
                break;
            case CONTEXT_MENU_ABOUT:
                Toast.makeText(BookListMainActivity.this,"图书列表",Toast.LENGTH_LONG).show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    private void InitData() {
        theBooks=new ArrayList<Book>();
        theBooks.add(new Book("软件项目管理案例教程（第4版）", R.drawable.book_2));
        theBooks.add(new Book("创新工程实践", R.drawable.book_no_name));
        theBooks.add(new Book("信息安全数学基础（第2版）", R.drawable.book_1));
    }

    public ArrayList<Book> getListBooks(){
        return theBooks;
    }

    protected class BooksArrayAdapter extends ArrayAdapter<Book> {
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

            Book book_item= (Book) this.getItem(position);
            img.setImageResource(book_item.getCoverResourceId());
            name.setText(book_item.getTitle());

            return item;

        }
    }
}
