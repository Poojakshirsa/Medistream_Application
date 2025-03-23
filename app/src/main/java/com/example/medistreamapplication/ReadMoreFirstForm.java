package com.example.medistreamapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.TextView;

public class ReadMoreFirstForm extends AppCompatActivity {

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_more_first_form);

        TextView noteTextView = findViewById(R.id.noteTextView);

        // Create a SpannableStringBuilder for the formatted text
        SpannableStringBuilder notes = new SpannableStringBuilder();


        // Add notes with bold main points
        addNoteWithFormatting(notes, "• Head", " - Abnormally large or small in size/shape deformity. Size check > 2SD < 2D");
        addNoteWithFormatting(notes, "• Eyes", " - Any visible abnormality i.e. white pupil, squint (important esp. after 3 months), frequent jerky movements, tilting the head when focusing (important esp. after 6 months)");
        addNoteWithFormatting(notes, "• Ear", " - Any abnormality of shape *do not refer if isolated finding");
        addNoteWithFormatting(notes, "• Lips and Palate", " - Cleft (One side or both side)");
        addNoteWithFormatting(notes, "• Neck", " - Exceptionally short *do not refer if isolated finding");
        addNoteWithFormatting(notes, "• Limbs", " - Any deformity/club foot");
        addNoteWithFormatting(notes, "• Spine", " - Neural tube defect");
        addNoteWithFormatting(notes, "• Features Suggestive of Down's Syndrome", "");
        addNoteWithFormatting(notes, "   # Eye", ": Upward slant of eyes (Imaginary line extended from the inner canthus to the outer cantus, goes below the outer canthus), and or epicanthic fold");
        addNoteWithFormatting(notes, "   # Nose", " - Depressed bridge");
        addNoteWithFormatting(notes, "   # Ears", " - Low set ears (Imaginary line extended from inner canthus and to the ear, passes above ear)");
        addNoteWithFormatting(notes, "   # Palm", " - Single crease across centre of palm (Simian crease)");
        addNoteWithFormatting(notes, "   # Feet", " - Wide gap (Cleft) between the great and first toe");
        addNoteWithFormatting(notes, "• Congenital Heart Disease", " - Any loud murmur on the chest or cyanosis on lips or bluish spells or features of congestive cardiac failure (Recurrent breathing difficulties, poor weight gain, exercise intolerance, easy fatigability, bilateral pitting edema)");

        // Set the formatted text to the TextView
        noteTextView.setText(notes);
    }

    private void addNoteWithFormatting(SpannableStringBuilder builder, String boldText, String normalText) {
        int start = builder.length();
        builder.append(boldText);
        builder.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(normalText);
        builder.append("\n\n"); // Add spacing between notes
    }
}
