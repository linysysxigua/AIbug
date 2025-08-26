# Mermaid Diagrams for AI Bug Prediction System

  ## 1. System Architecture Flow

  ```mermaid
  graph TB
      subgraph "Data Ingestion Layer"
          JIRA[JIRA API<br/>Bug Tickets & Descriptions]
          GitHub[GitHub API<br/>Pull Requests & Code]
          Sync[Sync Service<br/>Periodic Updates]
      end

      subgraph "AI Processing Engine"
          Embed[OpenAI Embeddings<br/>text-embedding-ada-002<br/>1536 dimensions]
          Store[(Vector Store<br/>In-Memory Cache)]
          Search[Similarity Search<br/>Cosine Similarity]
          Risk[Risk Calculator<br/>Weighted Scoring]
          Verify[Verification Engine<br/>180-day tracking]
      end

      subgraph "Integration Layer"
          API[FastAPI<br/>REST Endpoints]
          Queue[Task Queue<br/>Background Jobs]
          Notif[Notification Service]
      end

      subgraph "User Interfaces"
          Web[Streamlit Dashboard<br/>Real-time Monitoring]
          CLI[CLI Tools<br/>Local Development]
          CICD[CI/CD Integration<br/>GitHub Actions]
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

      PENDING --> HIT: Bug Linked<br/>(PR reference found)
      PENDING --> EXPIRED: 180 Days<br/>No Bugs

      HIT --> [*]: Verified Success
      EXPIRED --> [*]: Verified Safe

      note right of NOT_MERGED
          - Analyze code patterns
          - Calculate risk score
          - Post warnings
      end note

      note right of PENDING
          - Start 180-day timer
          - Monitor new bugs
          - Check PR references
      end note

      note right of HIT
          - Prediction confirmed
          - Update accuracy metrics
          - Learn from success
      end note

      note right of EXPIRED
          - No bugs found
          - Adjust model weights
          - Learn from false positive
      end note

  3. Real-World Case Study Sequence

  sequenceDiagram
      participant Dev as Developer
      participant GH as GitHub
      participant AI as AI System
      participant JIRA as JIRA
      participant Verify as Verification

      Dev->>GH: Submit PR #28<br/>"Enhanced Auth Service"
      GH->>AI: Webhook Trigger
      AI->>AI: Analyze Code Patterns
      Note over AI: Found: SQL concatenation<br/>Regex in loop<br/>Unclosed resources
      AI->>AI: Search Similar Bugs
      Note over AI: Match: SCRUM-18 (SQL injection)<br/>SCRUM-22 (Performance)<br/>SCRUM-14 (Memory leak)
      AI->>GH: Risk Score: 85/100<br/>⚠️ HIGH RISK
      AI->>JIRA: Post Risk Assessment
      Dev->>GH: Merge PR (ignored warning)
      GH->>Verify: Start 180-day monitoring
      Note over Verify: Day 7...
      JIRA->>Verify: New Bug: SQL Injection!<br/>References PR #28
      Verify->>AI: ✅ PREDICTION CONFIRMED
      AI->>AI: Update model confidence

  4. Integration Workflow

  graph LR
      subgraph "Development Flow"
          IDE[IDE Plugin<br/>Real-time Hints] --> PR[Pull Request]
          PR --> Analysis[AI Analysis]
      end

      subgraph "CI/CD Pipeline"
          Analysis --> GHA[GitHub Actions<br/>Risk Check]
          GHA --> Gate{Risk Gate}
          Gate -->|High Risk| Review[Manual Review]
          Gate -->|Low Risk| Auto[Auto-Approve]
      end

      subgraph "Monitoring"
          Review --> Merge[Merge to Main]
          Auto --> Merge
          Merge --> Track[180-day Tracking]
          Track --> Metrics[Update Metrics]
      end

      subgraph "Feedback Loop"
          Metrics --> Train[Retrain Model]
          Train --> Analysis
      end

  5. Simple Data Flow (From Earlier)

  graph LR
      subgraph "Input Sources"
          A[JIRA Bugs]
          B[GitHub PRs]
      end

      subgraph "AI Brain"
          C[Convert to Embeddings]
          D[Find Similar Patterns]
          E[Calculate Risk Score]
      end

      subgraph "Output"
          F[Web Dashboard]
          G[JIRA Comments]
          H[Slack Alerts]
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
      Start[New PR Submitted] --> Extract[Extract Code Changes]
      Extract --> Embed[Generate Embeddings]

      Embed --> Sim[Calculate Similarity]
      Sim --> S1[Find Top 10 Similar Bugs]

      S1 --> Factors[Collect Risk Factors]

      Factors --> F1[Similarity Score: 40%]
      Factors --> F2[Bug Severity: 25%]
      Factors --> F3[Code Complexity: 20%]
      Factors --> F4[Author Experience: 10%]
      Factors --> F5[File Sensitivity: 5%]

      F1 --> Calculate[Calculate Weighted Score]
      F2 --> Calculate
      F3 --> Calculate
      F4 --> Calculate
      F5 --> Calculate

      Calculate --> Score[Risk Score: 0-100]

      Score --> Decision{Risk Level?}
      Decision -->|0-40| Low[🟢 Low Risk]
      Decision -->|40-70| Medium[🟡 Medium Risk]
      Decision -->|70-100| High[🔴 High Risk]

      Low --> Notify[Post to JIRA/GitHub]
      Medium --> Notify
      High --> Notify

  7. Verification Process Flow

  graph TD
      Merge[PR Merged] --> Start[Start 180-day Timer]
      Start --> Monitor[Monitor New JIRA Issues]

      Monitor --> Check{Check for PR Reference}

      Check -->|Found "PR #X"| Hit[Mark as HIT]
      Check -->|Found "pull/X"| Hit
      Check -->|Not Found| Continue[Continue Monitoring]

      Continue --> Time{180 Days Passed?}
      Time -->|No| Monitor
      Time -->|Yes| Expire[Mark as EXPIRED]

      Hit --> Update1[Update JIRA Comment]
      Hit --> Learn1[Reinforce Model]
      Hit --> Metric1[Update Hit Rate]

      Expire --> Update2[Update JIRA Comment]
      Expire --> Learn2[Adjust Model Weights]
      Expire --> Metric2[Update Miss Rate]

  8. Future Architecture with Enhancements

  graph TB
      subgraph "Enhanced Data Sources"
          JIRA2[JIRA API]
          GH2[GitHub Webhooks<br/>Real-time]
          CI[CI/CD Metrics]
          IDE2[IDE Telemetry]
      end

      subgraph "Advanced AI Layer"
          Chunk[Code Chunking<br/>Large File Support]
          FAISS[FAISS Vector DB<br/>Million-scale]
          Custom[Custom Models<br/>Per Language]
          Fed[Federated Learning<br/>Cross-org]
      end

      subgraph "MCP Integration"
          MCP[Model Context Protocol]
          Claude[Claude Integration]
          GPT[ChatGPT Integration]
      end

      subgraph "Outputs"
          Dash[Dashboard]
          Action[GitHub Actions]
          IDE3[IDE Plugins]
          API2[REST API]
      end

      JIRA2 --> Chunk
      GH2 --> Chunk
      CI --> Chunk
      IDE2 --> Chunk

      Chunk --> FAISS
      FAISS --> Custom
      Custom --> Fed

      Fed --> MCP
      MCP --> Claude
      MCP --> GPT

      Fed --> Dash
      Fed --> Action
      Fed --> IDE3
      Fed --> API2
