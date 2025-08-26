graph TB
      subgraph "Data Sources"
          A[JIRA Bug Tickets]
          B[GitHub Pull Requests]
      end

      subgraph "AI Processing Engine"
          C[Text Embeddings<br/>OpenAI Ada-002]
          D[Vector Storage<br/>In-Memory Cache]
          E[Similarity Search<br/>Cosine Distance]
          F[Risk Calculator<br/>Multi-factor Scoring]
          G[Verification Tracker<br/>180-day Monitor]
      end

      subgraph "API Layer"
          H[FastAPI Backend<br/>REST Endpoints]
          I[Background Tasks<br/>Async Processing]
      end

      subgraph "User Interfaces"
          J[Streamlit Dashboard<br/>Real-time Monitoring]
          K[JIRA Integration<br/>Auto Comments]
          L[GitHub Actions<br/>CI/CD Gates]
          M[Slack Notifications<br/>Risk Alerts]
      end

      A --> C
      B --> C
      C --> D
      D --> E
      E --> F
      F --> G
      G --> H
      H --> I
      I --> J
      I --> K
      I --> L
      I --> M
      K --> A
      L --> B
