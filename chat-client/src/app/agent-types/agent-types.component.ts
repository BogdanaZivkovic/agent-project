import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { AgentService } from '../service/agent.service';

@Component({
  selector: 'app-agent-types',
  templateUrl: './agent-types.component.html',
  styleUrls: ['./agent-types.component.css']
})
export class AgentTypesComponent implements OnInit {

  constructor(public agentService : AgentService) { }

  ngOnInit(): void {
    this.agentService.getAgentTypes();
  }

  sortData(sort: Sort) {
    const data = this.agentService.agentTypes.slice();
    if (!sort.active || sort.direction === '') {
      this.agentService.agentTypes = data;
      return;
    }

    this.agentService.agentTypes = data.sort((a, b) => {
      const isAsc = sort.direction === 'asc';
      switch (sort.active) {
       // case 'agentType': return compare(a, b, isAsc);
        default: return 0;
      }
    });
  }
}

function compare(a: number | string, b: number | string, isAsc: boolean) {
  return (a < b ? -1 : 1) * (isAsc ? 1 : -1);
}
