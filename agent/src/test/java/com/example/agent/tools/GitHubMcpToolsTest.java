package com.example.agent.tools;

import com.example.agent.mcp.McpHttpClient;
import com.example.agent.tools.github.GitHubMcpTools;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GitHubMcpToolsTest {

    @Test
    void should_call_mcp_tool() {
        McpHttpClient mcp = mock(McpHttpClient.class);

        when(mcp.callTool(eq("issue_write"), anyMap()))
                .thenReturn(Mono.just(Map.of(
                        "number", 42,
                        "html_url", "https://github.com/x/y/issues/42"
                )));

        GitHubMcpTools tools = new GitHubMcpTools(mcp, "x", "y");
        String result = tools.createIssue("title", "body");

        assertTrue(result.contains("Issue created successfully"));

        @SuppressWarnings("unchecked")
        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);

        verify(mcp).callTool(eq("issue_write"), captor.capture());

        Map<String, Object> args = captor.getValue();
        assertEquals("x", args.get("owner"));
        assertEquals("y", args.get("repo"));
        assertEquals("title", args.get("title"));
        assertEquals("body", args.get("body"));
    }
}