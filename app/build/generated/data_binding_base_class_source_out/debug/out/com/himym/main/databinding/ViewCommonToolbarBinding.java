// Generated by view binder compiler. Do not edit!
package com.himym.main.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.himym.main.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ViewCommonToolbarBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final ConstraintLayout clToolBar;

  @NonNull
  public final AppCompatImageView ivBack;

  @NonNull
  public final AppCompatImageView ivRight;

  @NonNull
  public final AppCompatTextView tvRight;

  @NonNull
  public final AppCompatTextView tvTitle;

  private ViewCommonToolbarBinding(@NonNull ConstraintLayout rootView,
      @NonNull ConstraintLayout clToolBar, @NonNull AppCompatImageView ivBack,
      @NonNull AppCompatImageView ivRight, @NonNull AppCompatTextView tvRight,
      @NonNull AppCompatTextView tvTitle) {
    this.rootView = rootView;
    this.clToolBar = clToolBar;
    this.ivBack = ivBack;
    this.ivRight = ivRight;
    this.tvRight = tvRight;
    this.tvTitle = tvTitle;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ViewCommonToolbarBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ViewCommonToolbarBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.view_common_toolbar, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ViewCommonToolbarBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      ConstraintLayout clToolBar = (ConstraintLayout) rootView;

      id = R.id.ivBack;
      AppCompatImageView ivBack = ViewBindings.findChildViewById(rootView, id);
      if (ivBack == null) {
        break missingId;
      }

      id = R.id.ivRight;
      AppCompatImageView ivRight = ViewBindings.findChildViewById(rootView, id);
      if (ivRight == null) {
        break missingId;
      }

      id = R.id.tvRight;
      AppCompatTextView tvRight = ViewBindings.findChildViewById(rootView, id);
      if (tvRight == null) {
        break missingId;
      }

      id = R.id.tvTitle;
      AppCompatTextView tvTitle = ViewBindings.findChildViewById(rootView, id);
      if (tvTitle == null) {
        break missingId;
      }

      return new ViewCommonToolbarBinding((ConstraintLayout) rootView, clToolBar, ivBack, ivRight,
          tvRight, tvTitle);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}