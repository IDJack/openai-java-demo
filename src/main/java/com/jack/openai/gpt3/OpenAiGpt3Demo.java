package com.jack.openai.gpt3;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theokanning.openai.OpenAiApi;
import com.theokanning.openai.completion.chat.ChatCompletionChoice;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.service.OpenAiService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OpenAiGpt3Demo {
    public static void main(String[] args) {
        // replace with your openai account token
        String token = "sk-xxxxxxxx";
        int timeout = 600;
        OpenAiApi api = buildApi(token, Duration.ofSeconds(timeout));
        OpenAiService service = new OpenAiService(api);

        String content = "Hello, what's your name";
        System.out.println("\nUser Input:" + content);
        String role = "user";
        ChatMessage message = new ChatMessage(role, content);
        List<ChatMessage> msgs= new ArrayList<>();
        msgs.add(message);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest.builder()
                .model("gpt-3.5-turbo")
                .messages(msgs)
                .build();

        System.out.println("\nOpenAi gpt-3.5 Output:");
        List<ChatCompletionChoice> choices = service.createChatCompletion(chatCompletionRequest).getChoices();
        System.out.println(choices.get(0).getMessage().getContent());
    }

    /**
     * customize OpenAiApi
     * @param token openai account token
     * @param timeout
     * @return customize OpenAiApi
     */
    private static OpenAiApi buildApi(String token, Duration timeout) {
        Objects.requireNonNull(token, "OpenAI token required");
        // replace with your proxy
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 58591));
        ObjectMapper mapper = OpenAiService.defaultObjectMapper();
        OkHttpClient client = OpenAiService.defaultClient(token, timeout)
                .newBuilder()
                .proxy(proxy)
                .build();
        Retrofit retrofit = OpenAiService.defaultRetrofit(client, mapper);
        return retrofit.create(OpenAiApi.class);
    }
}
