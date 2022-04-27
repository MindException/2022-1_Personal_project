package com.lastproject.used_item_market;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecycleSellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Product> productArrayList = new ArrayList<>();

    RecycleSellAdapter(){}

    RecycleSellAdapter(List<Product> productArrayList){    //새로운 쿼리용 생성자

        this.productArrayList = productArrayList;

    }

    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ  어뎁터 눌렸울 경우 ㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    public interface OnItemClickListener            //아이템이 눌린
    {
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;  //클릭 리스너 변수

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }
    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ 여기까지 ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { // 아이템 뷰를 위한 뷰 객체 생성 후 리턴
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        // layoutInflater는 미리 정의해둔 xml 실제 메모리에 올리는 것.
        // flater는 부풀리다라는 뜻.
        // inflate(xml파일, 뷰를 넣은 레이아웃, 인플레이션 여부)
        return new ViewHolderSellProduct(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((ViewHolderSellProduct)holder).onBind(productArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    void addItem(Product data) {      //아이템을 여기다가 집어넣는다.

        productArrayList.add(data);

    }

    //아이템 저장하는 클래스
    class ViewHolderSellProduct extends RecyclerView.ViewHolder{

        ImageView iv;
        TextView title;
        TextView price;
        TextView date;

        public ViewHolderSellProduct(@NonNull View itemView) {

            super(itemView);


            itemView.setOnClickListener(new View.OnClickListener() {            //클릭은 여기다가 한다.
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)            //눌렸을 경우
                    {
                        // click event
                        mListener.onItemClick(v, pos);
                    }

                }
            });

            //아이템 연결
            iv = (ImageView) itemView.findViewById(R.id.itemimg);
            title = (TextView) itemView.findViewById(R.id.title_text);
            title.setSelected(true); // 긴 문장 흘러서 보여줌.
            price = (TextView) itemView.findViewById(R.id.price_text);
            price.setSelected(true); // 긴 문장 흘러서 보여줌.
            date = (TextView) itemView.findViewById(R.id.date_text);

        }

        public void onBind(Product pdt){

            title.setText(pdt.title);
            price.setText(Long.toString(pdt.cost) + "원");
            date.setText(pdt.time);

        }
    }

}
