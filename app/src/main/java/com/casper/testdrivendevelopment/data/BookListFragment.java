package com.casper.testdrivendevelopment.data;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.casper.testdrivendevelopment.BookListMainActivity;
import com.casper.testdrivendevelopment.R;

/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("ValidFragment")
public class BookListFragment extends Fragment {

    private BookListMainActivity.BooksArrayAdapter bookAdapter;

    public BookListFragment(BookListMainActivity.BooksArrayAdapter bookAdapter) {
        this.bookAdapter=bookAdapter;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);
        ListView list_view_books = (ListView) view.findViewById(R.id.list_view_books);
        list_view_books.setAdapter(bookAdapter);
        this.registerForContextMenu(list_view_books);
        return view;
    }

}
