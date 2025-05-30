package com.example.learningapp;

import android.os.Parcel;
import android.os.Parcelable;

public class ResultItem implements Parcelable {
    public String question;
    public String correctAnswer;
    public String userAnswer;

    public ResultItem(String question, String correctAnswer, String userAnswer) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
    }

    protected ResultItem(Parcel in) {
        question = in.readString();
        correctAnswer = in.readString();
        userAnswer = in.readString();
    }

    public static final Creator<ResultItem> CREATOR = new Creator<ResultItem>() {
        @Override
        public ResultItem createFromParcel(Parcel in) {
            return new ResultItem(in);
        }

        @Override
        public ResultItem[] newArray(int size) {
            return new ResultItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(correctAnswer);
        parcel.writeString(userAnswer);
    }
}
