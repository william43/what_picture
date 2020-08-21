package com.example.willaboy.whatpicture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DimenRes;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Activity_PuzzleGame extends AppCompatActivity {

    private static int chunkNumbers = 0;
    private GridView grid;
    private static ImageView image;
    private DatabaseReference mDatabaseRef;
    private List<Model_Upload> mUploads;
    private static int check = 0;


    private static GestureDetectGridView mGridView;

    private static final int COLUMNS = 4;
    private static final int DIMENSIONS = COLUMNS * COLUMNS;

    private static int mColumnWidth, mColumnHeight;

    public static final String up = "up";
    public static final String down = "down";
    public static final String left = "left";
    public static final String right = "right";

    private static int[] tileList;
    private static ArrayList<Bitmap> chunkeds = new ArrayList<>();


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.playpuzzle_view);

        image = (ImageView) findViewById(R.id.source_image);



        init();

        //scramble();
        shuffle(tileList);
        while(getInvCount(DIMENSIONS) % 2 != 0){
            //scramble();
            shuffle(tileList);
        }

        setDimensions();




    }

    private static void splitImage(ImageView image, int chunkNumbers, ArrayList<Bitmap> chunkeds) {

        //For the number of rows and columns of the grid to be displayed
        int rows,cols;

        //For height and width of the small image chunks
        int chunkHeight,chunkWidth;


//        Log.d("MAMAMO", mUploads.get(0).getName() + " HEHEHEHEH " + mUploads.get(0).getImageUrl() + " HEHEHE");

//        //To store all the small image chunks in bitmap format in this list
//        ArrayList<Bitmap> chunkedImages = new ArrayList<Bitmap>(chunkNumbers);
////
////        //Getting the scaled bitmap of the source image
        BitmapDrawable drawable = (BitmapDrawable) image.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
//
        rows = cols = (int) Math.sqrt(chunkNumbers);
        chunkHeight = bitmap.getHeight()/rows;
        chunkWidth = bitmap.getWidth()/cols;
//
//        //xCoord and yCoord are the pixel positions of the image chunks
        int yCoord = 0;
        for(int x=0; x<rows; x++){
            int xCoord = 0;
            for(int y=0; y<cols; y++){
                chunkeds.add(Bitmap.createBitmap(scaledBitmap, xCoord, yCoord, chunkWidth, chunkHeight));
                xCoord += chunkWidth;
            }
            yCoord += chunkHeight;
        }
//
        image.setVisibility(View.INVISIBLE);
//        return chunkedImages;
//        grid.setAdapter(new PuzzleAdapter(this, chunkedImages));
//        grid.setNumColumns((int) Math.sqrt(chunkedImages.size()));
//        grid.setVisibility(View.VISIBLE);

        //Start a new activity to show these chunks into a grid
//        Intent intent = new Intent(MainActivity.this, ChunkedImageActivity.class);
//        intent.putParcelableArrayListExtra("image chunks", chunkedImages);
//        startActivity(intent);
    }


    private void init() {
        mGridView = (GestureDetectGridView) findViewById(R.id.grid);
        mGridView.setNumColumns(COLUMNS);

        tileList = new int[DIMENSIONS];
        for (int i = 0; i < DIMENSIONS; i++) {
            tileList[i] = i;
        }
    }

    private void scramble() {
        int index;
        int temp;
        Random random = new Random();

        for (int i = tileList.length - 1; i > 0; i--) {
            index = random.nextInt(i + 1);
            temp = tileList[index];
            tileList[index] = tileList[i];
            tileList[i] = temp;
        }
    }

    private void shuffle(int[] array) {
        int n = array.length;
        Random random = new Random();
        // Loop over array.
        for (int i = 0; i < array.length; i++) {
            // Get a random index of the array past the current index.
            // ... The argument is an exclusive bound.
            //     It will not go past the array's end.
            int randomValue = i + random.nextInt(n - i);
            // Swap the random element with the present element.
            int randomElement = array[randomValue];
            array[randomValue] = array[i];
            array[i] = randomElement;
        }
    }



    private static int getInvCount(int n) {
        int inv_count = 0;
        for (int i = 0; i < n - 1; i++){
            for (int j = i + 1; j < n; j++) {
                if (tileList[i] > tileList[j]) {
                    if (tileList[i] != DIMENSIONS -1 && tileList[j] != DIMENSIONS-1) {
                        inv_count++;
                    }
                }
            }
        }



        Log.d("MAMAMO", "MAMAMO INVERSIONS" + inv_count);
        return inv_count;
    }

    private void setDimensions() {
        ViewTreeObserver vto = mGridView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                mGridView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int displayWidth = mGridView.getMeasuredWidth();
                int displayHeight = mGridView.getMeasuredHeight();

                int statusbarHeight = getStatusBarHeight(getApplicationContext());
                int requiredHeight = displayHeight - statusbarHeight;

                mColumnWidth = displayWidth / COLUMNS;
                mColumnHeight = requiredHeight / COLUMNS;

                display(getApplicationContext());
            }
        });
    }

    private int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen",
                "android");

        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }

        return result;
    }

    private static void display(final Context context) {
        final ArrayList<ImageView> buttons = new ArrayList<>();
//        final Button button


        DatabaseReference mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        final List<Model_Upload> mUploads = new ArrayList<>();



        Log.d("MAMAMO", mUploads.size() + " HEHEHEHEHEH");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Model_Upload upload = postSnapshot.getValue(Model_Upload.class);
                    mUploads.add(upload);
                }


                Picasso.with(context)
                        .load(mUploads.get(0).getImageUrl())
                        .fit()
                        .centerCrop()
                        .into(image, new com.squareup.picasso.Callback(){
                                    @Override
                                    public void onSuccess(){
                                        Toast.makeText(context, "Picture Loaded", Toast.LENGTH_LONG).show();
                                        chunkNumbers = DIMENSIONS;
                                        if(check == 0) {
                                            splitImage(image, chunkNumbers, chunkeds);
                                            check = 1;
                                        }

                                        for (int i = 0; i < tileList.length; i++) {
                                            ImageView button = new ImageView(context);

                                            if (tileList[i] == 0) {
                                                button.setImageBitmap(chunkeds.get(0));
                                                //button.setImageResource(R.drawable.pigeon_piece1);
                                            } else if (tileList[i] == 1) {
                                                button.setImageBitmap(chunkeds.get(1));
                                                //button.setImageResource(R.drawable.pigeon_piece2);
                                            }
                                            else if (tileList[i] == 2) {
                                                button.setImageBitmap(chunkeds.get(2));
                                                //button.setImageResource(R.drawable.pigeon_piece3);
                                            }
                                            else if (tileList[i] == 3) {
                                                button.setImageBitmap(chunkeds.get(3));
                                                //button.setImageResource(R.drawable.pigeon_piece4);
                                            }
                                            else if (tileList[i] == 4) {
                                                button.setImageBitmap(chunkeds.get(4));
                                                //button.setImageResource(R.drawable.pigeon_piece5);
                                            }
                                            else if (tileList[i] == 5) {
                                                button.setImageBitmap(chunkeds.get(5));
                                                //button.setImageResource(R.drawable.pigeon_piece6);
                                            }
                                            else if (tileList[i] == 6) {
                                                button.setImageBitmap(chunkeds.get(6));
                                                //button.setImageResource(R.drawable.pigeon_piece7);
                                            }
                                            else if (tileList[i] == 7) {
                                                button.setImageBitmap(chunkeds.get(7));
                                                //button.setImageResource(R.drawable.pigeon_piece8);
                                            }
                                            else if (tileList[i] == 8) {
                                                //button.setImageBitmap(chunkeds.get(8));
                                                if(COLUMNS == 3)
                                                    button.setImageResource(R.drawable.empty_slot);
                                                else
                                                    button.setImageBitmap(chunkeds.get(8));
                                            }
                                            else if(tileList[i] == 9){
                                                button.setImageBitmap(chunkeds.get(9));
                                            }
                                            else if(tileList[i] == 10){
                                                button.setImageBitmap(chunkeds.get(10));
                                            }
                                            else if(tileList[i] == 11){
                                                button.setImageBitmap(chunkeds.get(11));
                                            }
                                            else if(tileList[i] == 12){
                                                button.setImageBitmap(chunkeds.get(12));
                                            }
                                            else if(tileList[i] == 13){
                                                button.setImageBitmap(chunkeds.get(13));
                                            }
                                            else if(tileList[i] == 14){
                                                button.setImageBitmap(chunkeds.get(14));
                                            }
                                            else if(tileList[i] == 15){
                                                if(COLUMNS == 4)
                                                    button.setImageResource(R.drawable.empty_slot);
                                                else
                                                    button.setImageBitmap(chunkeds.get(15));
                                            }
                                            else if(tileList[i] == 16){
                                                button.setImageBitmap(chunkeds.get(16));
                                            }
                                            else if(tileList[i] == 17){
                                                button.setImageBitmap(chunkeds.get(17));
                                            }
                                            else if(tileList[i] == 18){
                                                button.setImageBitmap(chunkeds.get(18));
                                            }
                                            else if(tileList[i] == 19){
                                                button.setImageBitmap(chunkeds.get(19));
                                            }
                                            else if(tileList[i] == 20){
                                                button.setImageBitmap(chunkeds.get(20));
                                            }
                                            else if(tileList[i] == 21){
                                                button.setImageBitmap(chunkeds.get(21));
                                            }
                                            else if(tileList[i] == 22){
                                                button.setImageBitmap(chunkeds.get(22));
                                            }
                                            else if(tileList[i] == 23){
                                                button.setImageBitmap(chunkeds.get(23));
                                            }
                                            else if(tileList[i] == 24){
                                                button.setImageResource(R.drawable.empty_slot);
                                            }

                                            buttons.add(button);
                                        }

                                        mGridView.setAdapter(new CustomAdapter(buttons, mColumnWidth, mColumnHeight));
                                        getInvCount(buttons.size());
                                    }
                                    @Override
                                    public void onError(){

                                    }
                                }
                        );

                Log.d("MAMAMO", mUploads.size() + " HEHEHEHEHEH");
                // mAdapter = new ImageAdapter(Activity_ViewPhotos.this, mUploads);

                //mRecyclerView.setAdapter(mAdapter);
                //mProgressCircle.setVisibility(View.INVISIBLE)
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                //mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });





    }
//
    private static void swap(Context context, int currentPosition, int swap) {
        if(tileList[currentPosition+swap] == DIMENSIONS-1) {
            int newPosition = tileList[currentPosition + swap];
            tileList[currentPosition + swap] = tileList[currentPosition];
            tileList[currentPosition] = newPosition;
            display(context);
        }
        else
            Toast.makeText(context, "Wrong move", Toast.LENGTH_LONG).show();

//        if (isSolved()) Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
    }



    public static void moveTiles(Context context, String direction, int position) {

        // Upper-left-corner tile

            if (position == 0) {

                if (direction.equals(right))
                    swap(context, position, 1);
                else if (direction.equals(down))
                    swap(context, position, COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Upper-center tiles
            } else if (position > 0 && position < COLUMNS - 1) {
                if (direction.equals(left)) swap(context, position, -1);
                else if (direction.equals(down)) swap(context, position, COLUMNS);
                else if (direction.equals(right)) swap(context, position, 1);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Upper-right-corner tile
            } else if (position == COLUMNS - 1) {
                if (direction.equals(left)) swap(context, position, -1);
                else if (direction.equals(down)) swap(context, position, COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Left-side tiles
            } else if (position > COLUMNS - 1 && position < DIMENSIONS - COLUMNS &&
                    position % COLUMNS == 0) {
                if (direction.equals(up)) swap(context, position, -COLUMNS);
                else if (direction.equals(right)) swap(context, position, 1);
                else if (direction.equals(down)) swap(context, position, COLUMNS);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Right-side AND bottom-right-corner tiles
            } else if (position == COLUMNS * 2 - 1 || position == COLUMNS * 3 - 1) {
                if (direction.equals(up)) swap(context, position, -COLUMNS);
                else if (direction.equals(left)) swap(context, position, -1);
                else if (direction.equals(down)) {

                    // Tolerates only the right-side tiles to swap downwards as opposed to the bottom-
                    // right-corner tile.
                    if (position <= DIMENSIONS - COLUMNS - 1) swap(context, position,
                            COLUMNS);
                    else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();
                } else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Bottom-left corner tile
            } else if (position == DIMENSIONS - COLUMNS) {
                if (direction.equals(up)) swap(context, position, -COLUMNS);
                else if (direction.equals(right)) swap(context, position, 1);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Bottom-center tiles
            } else if (position < DIMENSIONS - 1 && position > DIMENSIONS - COLUMNS) {
                if (direction.equals(up)) swap(context, position, -COLUMNS);
                else if (direction.equals(left)) swap(context, position, -1);
                else if (direction.equals(right)) swap(context, position, 1);
                else Toast.makeText(context, "Invalid move", Toast.LENGTH_SHORT).show();

                // Center tiles
            } else {
                if (direction.equals(up)) swap(context, position, -COLUMNS);
                else if (direction.equals(left)) swap(context, position, -1);
                else if (direction.equals(right)) swap(context, position, 1);
                else swap(context, position, COLUMNS);
            }
    }
//
    private static boolean isSolved() {
        boolean solved = false;

        for (int i = 0; i < tileList.length; i++) {
            if (tileList[i] == i) {
                solved = true;
            } else {
                solved = false;
                break;
            }
        }

        return solved;
    }


}
