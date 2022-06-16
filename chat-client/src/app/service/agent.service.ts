import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Agent } from '../model/agent';

const baseUrl = 'http://localhost:8080/Chat-war/api/agents/';

@Injectable({
  providedIn: 'root'
})
export class AgentService {

  agents: Agent[] = [];
  agentTypes: String [] = [];

  constructor(private http : HttpClient) {}

  getRunningAgents() {
    return this.http.get(baseUrl + "running").subscribe();
  }

  getAgentTypes() {
    return this.http.get(baseUrl + "classes").subscribe();
  }
}
