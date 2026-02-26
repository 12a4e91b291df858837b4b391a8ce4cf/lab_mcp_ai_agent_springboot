package com.example.agent.web;

import com.example.agent.service.AgentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Map;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AgentControllerIT {

    @Autowired
    WebTestClient web;

    @MockitoBean
    AgentService agentService;

    @Test
    void should_call_endpoint() {
        when(agentService.run(eq("Create a task to add OpenTelemetry")))
                .thenReturn("Issue created successfully: {number=1, html_url=https://github.com/o/r/issues/1}");

        web.post().uri("/api/run")
                .bodyValue(Map.of("prompt", "Create a task to add OpenTelemetry"))
                .exchange()
                .expectStatus().isOk();
    }
}