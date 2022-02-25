	package ru.ointeractive.androdesign.widget;
  /*
   Created by Acuna on 21.08.2021
  */
	
	import android.content.Context;
	import android.support.annotation.NonNull;
	import android.util.AttributeSet;
	import android.view.LayoutInflater;
	import android.view.View;
	import android.view.ViewGroup;
	
	import upl.core.Log;
	
	public class RateView extends RecyclerView {
		
		protected int imagesNum = 5, selectNum = 0, i2;
		protected int mImageId, mInactiveImageId;
		protected int mLayout;
		
		public RateView (Context context) {
			this (context, null);
		}
		
		public RateView (Context context, AttributeSet attrs) {
			this (context, attrs, 0);
		}
		
		public RateView (Context context, AttributeSet attrs, int defStyle) {
			super (context, attrs, defStyle);
		}
		
		public RateView show () {
			
			setLayoutManager (new LinearLayoutManager (getContext (), LinearLayoutManager.HORIZONTAL, false));
			
			setAdapter (new Adapter ());
			
			return this;
			
		}
		
		public RateView setImage (int id) {
			
			mImageId = id;
			return this;
			
		}
		
		public RateView setInactiveImage (int id) {
			
			mInactiveImageId = id;
			return this;
			
		}
		
		public RateView setImagesNum (int num) {
			
			imagesNum = num;
			return this;
			
		}
		
		public RateView setLayout (int layout) {
			
			mLayout = layout;
			return this;
			
		}
		
		public int getSelectNum () {
			return selectNum;
		}
		
		private class Adapter extends android.support.v7.widget.RecyclerView.Adapter<ViewHolder> {
			
			@Override
			@NonNull
			public ViewHolder onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
				return new ViewHolder (LayoutInflater.from (parent.getContext ()).inflate (mLayout, parent, false));
			}
			
			@Override
			public void onBindViewHolder (@NonNull final ViewHolder holder, int position) {
				
				holder.inactiveImageView.setOnClickListener (new OnClickListener () {
					
					@Override
					public void onClick (View view) {
						onClick2 (holder);
					}
					
				});
				
				holder.imageView.setOnClickListener (new OnClickListener () {
					
					@Override
					public void onClick (View view) {
						onClick2 (holder);
					}
					
				});
				
			}
			
			private void onClick2 (ViewHolder holder) {
				
				selectNum = holder.getAdapterPosition () + 1;
				
				for (int i = 0; i < getItemCount (); i++) {
					
					ViewHolder holder2 = (ViewHolder) getChildViewHolder (getChildAt (i));
					
					holder2.inactiveImageView.setVisibility (View.VISIBLE);
					holder2.imageView.setVisibility (View.GONE);
					
				}
				
				for (int i = 0; i < getItemCount (); i++) {
					
					ViewHolder holder2 = (ViewHolder) getChildViewHolder (getChildAt (i));
					
					if (i < selectNum) { // Активные
						
						holder2.inactiveImageView.setVisibility (View.GONE);
						holder2.imageView.setVisibility (View.VISIBLE);
						
					}
					
				}
				
			}
			
			@Override
			public int getItemCount () {
				return imagesNum;
			}
			
		}
		
		private class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
			
			public View imageView, inactiveImageView;
			
			public ViewHolder (View view) {
				
				super (view);
				
				imageView = view.findViewById (mImageId);
				inactiveImageView = view.findViewById (mInactiveImageId);
				
			}
			
		}
		
	}