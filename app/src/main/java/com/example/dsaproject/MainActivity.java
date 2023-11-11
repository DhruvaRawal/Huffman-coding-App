package com.example.dsaproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class MainActivity extends AppCompatActivity {

    private Map<Character, String> codes = new HashMap<>();
    private PriorityQueue<MinHeapNode> minHeap;

    private static class MinHeapNode {
        char data;
        int freq;
        MinHeapNode left, right;

        MinHeapNode(char data, int freq) {
            this.data = data;
            this.freq = freq;
            this.left = null;
            this.right = null;
        }
    }

    private static void printCodes(MinHeapNode root, String str, StringBuilder sb, Map<Character, String> codes) {
        if (root == null)
            return;
        if (root.data != '$') {
            sb.append(root.data).append(": ").append(str).append("\n");
            codes.put(root.data, str);
        }
        printCodes(root.left, str + "0", sb, codes);
        printCodes(root.right, str + "1", sb, codes);
    }
    private static void calcFreq(String str, int n, Map<Character, Integer> freq) {
        for (int i = 0; i < n; i++) {
            char c = str.charAt(i);
            freq.put(c, freq.getOrDefault(c, 0) + 1);
        }
    }

    private void HuffmanCodes(String input) {
        Map<Character, Integer> freq = new HashMap<>();
        calcFreq(input, input.length(), freq);

        minHeap = new PriorityQueue<>((l, r) -> l.freq - r.freq);

        for (Map.Entry<Character, Integer> entry : freq.entrySet()) {
            minHeap.add(new MinHeapNode(entry.getKey(), entry.getValue()));
        }

        while (minHeap.size() != 1) {
            MinHeapNode left = minHeap.poll();
            MinHeapNode right = minHeap.poll();
            MinHeapNode top = new MinHeapNode('$', left.freq + right.freq);
            top.left = left;
            top.right = right;
            minHeap.add(top);
        }

        StringBuilder codesString = new StringBuilder();
        printCodes(minHeap.peek(), "", codesString, codes);
        System.out.println("Character With their Frequencies:");
        System.out.println(codesString);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        EditText inputText = findViewById(R.id.input_text);
        Button encodeButton = findViewById(R.id.encode_button);
        TextView encodedText = findViewById(R.id.encoded_text);
        Button decodeButton = findViewById(R.id.decode_button);
        TextView decodedText = findViewById(R.id.decoded_text);

        encodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputText.getText().toString();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a string to encode", Toast.LENGTH_SHORT).show();
                } else {
                    HuffmanCodes(input);
                    StringBuilder encodedString = new StringBuilder();
                    for (char c : input.toCharArray()) {
                        encodedString.append(codes.get(c));
                    }
                    encodedText.setText("Encoded Huffman data:\n" + encodedString.toString());
                }
            }
        });

        decodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = encodedText.getText().toString();
                if (input.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please encode a string first", Toast.LENGTH_SHORT).show();
                } else {
                    String encodedString = encodedText.getText().toString().split("\n")[1];
                    String decodedString = decode_file(minHeap.peek(), encodedString);
                    decodedText.setText("Decoded Huffman data:\n" + decodedString);
                }
            }
        });
    }

    private static String decode_file(MinHeapNode root, String encodedString) {
        StringBuilder decodedString = new StringBuilder();
        MinHeapNode current = root;

        for (int i = 0; i < encodedString.length(); i++) {
            char bit = encodedString.charAt(i);
            if (bit == '0') {
                current = current.left;
            } else {
                current = current.right;
            }

            if (current.left == null && current.right == null) {
                decodedString.append(current.data);
                current = root;
            }
        }

        return decodedString.toString();
    }

}

/*
             /     \/
    |¯¯¯¯¯¯/|      /\
    |    /  |     /  \                        /\
    |  /    |    /    \                      /  \
    |/______|  _/______\_                   /    \
   /                                       /¯¯¯\  \
    \\/\  /\    /\  /\//                  /     \  \
     \  \/  \  /  \/  /                  /       \  \
      \  /\  \//\    /                  /         \ /
       \/  \  /  \  /                  /           V
            \/    \/                  /            *
                                     /           ** *
                                    /           *  ** *
                                   /          *  **  *  *
*/









