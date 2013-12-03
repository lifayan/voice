package com.springapp.mvc.domain;

import com.voxeo.tropo.enums.Voice;

public class UpdateDemo {
    private String message;
    private Voice voice;
    private String answer;

    public UpdateDemo() {
    }

    public UpdateDemo(String message, String answer, Voice voice) {
        this.message = message;
        this.answer = answer;
        this.voice = voice;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Voice getVoice() {
        return voice;
    }

    public void setVoice(Voice voice) {
        this.voice = voice;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
