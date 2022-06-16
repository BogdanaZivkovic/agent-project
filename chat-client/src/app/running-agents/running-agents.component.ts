import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { AgentService } from '../service/agent.service';
import { UserService } from '../service/user.service';

@Component({
  selector: 'app-running-agents',
  templateUrl: './running-agents.component.html',
  styleUrls: ['./running-agents.component.css']
})
export class RunningAgentsComponent implements OnInit {

  constructor(public agentService : AgentService, public userService : UserService) { }

  ngOnInit(): void {
    this.agentService.getRunningAgents();
  }

  sortData(sort: Sort) {
    const data = this.agentService.agents.slice();
    if (!sort.active || sort.direction === '') {
      this.agentService.agents = data;
      return;
    }

    this.agentService.agents = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
        case 'name': return compare(a.name, b.name, isAsc);
        case 'type': return compare(a.type, b.type, isAsc);
        case 'hostAddress': return compare(a.hostAddress, b.hostAddress, isAsc);
        case 'hostAlias': return compare(a.hostAlias, b.hostAlias, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}