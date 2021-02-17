package com.example.netdepression.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaylistBlock extends Block{

   @SerializedName("uiElement")
   public BlockUiElement blockUiElement;

   public List<Creative> creatives;

}
