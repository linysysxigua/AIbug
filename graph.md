1. System Architecture Flow

  graph TB
      subgraph "Data Ingestion"
          JIRA[JIRA API]
          GitHub[GitHub API]
          Sync[Sync Service]
      end

      subgraph "AI Processing"
          Embed[OpenAI Embeddings]
          Store[(Vector Store)]
          Search[Similarity Search]
          Risk[Risk Calculator]
          Verify[Verification Engine]
      end

      subgraph "Integration"
          API[FastAPI]
          Queue[Task Queue]
          Notif[Notifications]
      end

      subgraph "Interfaces"
          Web[Streamlit Dashboard]
          CLI[CLI Tools]
          CICD[CI/CD]
      end

      JIRA --> Sync
      GitHub --> Sync
      Sync --> Embed
      Embed --> Store
      Store --> Search
      Search --> Risk
      Risk --> Verify
      Verify --> API
      API --> Queue
      Queue --> Notif
      API --> Web
      API --> CLI
      API --> CICD
      Notif --> JIRA
      Notif --> GitHub

  2. Verification State Machine

  stateDiagram-v2
      [*] --> NOT_MERGED: PR Created
      NOT_MERGED --> NOT_MERGED: Code Changes
      NOT_MERGED --> PENDING: PR Merged
      NOT_MERGED --> [*]: PR Closed
      PENDING --> HIT: Bug Found
      PENDING --> EXPIRED: 180 Days Pass
      HIT --> [*]: Verified
      EXPIRED --> [*]: No Bugs

  3. PR Analysis Sequence

  sequenceDiagram
      participant Dev
      participant GitHub
      participant AI
      participant JIRA
      participant Verify

      Dev->>GitHub: Submit PR
      GitHub->>AI: Trigger Analysis
      AI->>AI: Analyze Patterns
      AI->>AI: Search Similar Bugs
      AI->>GitHub: Post Risk Score
      AI->>JIRA: Post Assessment
      Dev->>GitHub: Merge PR
      GitHub->>Verify: Start Monitoring
      JIRA->>Verify: New Bug Report
      Verify->>AI: Confirm Prediction
      AI->>AI: Update Model

  4. Integration Workflow

  graph LR
      subgraph Development
          IDE[IDE Plugin]
          PR[Pull Request]
          Analysis[AI Analysis]
      end

      subgraph Pipeline
          GHA[GitHub Actions]
          Gate{Risk Gate}
          Review[Manual Review]
          Auto[Auto Approve]
      end

      subgraph Monitoring
          Merge[Merge to Main]
          Track[180-day Track]
          Metrics[Update Metrics]
      end

      IDE --> PR
      PR --> Analysis
      Analysis --> GHA
      GHA --> Gate
      Gate -->|High| Review
      Gate -->|Low| Auto
      Review --> Merge
      Auto --> Merge
      Merge --> Track
      Track --> Metrics

  5. Data Flow Overview

  graph LR
      subgraph Input
          A[JIRA Bugs]
          B[GitHub PRs]
      end

      subgraph Processing
          C[Embeddings]
          D[Similarity]
          E[Risk Score]
      end

      subgraph Output
          F[Dashboard]
          G[Comments]
          H[Alerts]
      end

      A --> C
      B --> C
      C --> D
      D --> E
      E --> F
      E --> G
      E --> H

  6. Risk Calculation Flow

  graph TD
      Start[New PR] --> Extract[Extract Code]
      Extract --> Embed[Generate Embeddings]
      Embed --> Sim[Calculate Similarity]
      Sim --> Factors[Collect Risk Factors]
      Factors --> F1[Similarity 40%]
      Factors --> F2[Severity 25%]
      Factors --> F3[Complexity 20%]
      Factors --> F4[Experience 10%]
      Factors --> F5[Sensitivity 5%]
      F1 --> Calc[Calculate Score]
      F2 --> Calc
      F3 --> Calc
      F4 --> Calc
      F5 --> Calc
      Calc --> Score[Risk Score 0-100]
      Score --> Level{Risk Level}
      Level -->|Low| Green[Low Risk]
      Level -->|Medium| Yellow[Medium Risk]
      Level -->|High| Red[High Risk]

  7. Verification Process

  graph TD
      Merged[PR Merged] --> Timer[Start 180-day Timer]
      Timer --> Monitor[Monitor JIRA]
      Monitor --> Check{PR Reference Found?}
      Check -->|Yes| Hit[Mark as HIT]
      Check -->|No| Continue[Continue]
      Continue --> Expired{180 Days?}
      Expired -->|No| Monitor
      Expired -->|Yes| Expire[Mark EXPIRED]
      Hit --> UpdateHit[Update Metrics]
      Expire --> UpdateExp[Update Metrics]

  8. Future Architecture

  graph TB
      subgraph Sources
          J[JIRA]
          G[GitHub Webhooks]
          C[CI/CD Metrics]
          I[IDE Telemetry]
      end

      subgraph AI
          Ch[Code Chunking]
          F[FAISS Vector DB]
          M[Custom Models]
          L[Federated Learning]
      end

      subgraph MCP
          P[MCP Protocol]
          Cl[Claude]
          Gp[ChatGPT]
      end

      subgraph Output
          D[Dashboard]
          A[Actions]
          E[IDE Plugins]
          R[REST API]
      end

      J --> Ch
      G --> Ch
      C --> Ch
      I --> Ch
      Ch --> F
      F --> M
      M --> L
      L --> P
      P --> Cl
      P --> Gp
      L --> D
      L --> A
      L --> E
      L --> R
