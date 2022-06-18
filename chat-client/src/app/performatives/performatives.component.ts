import { Component, OnInit } from '@angular/core';
import { Sort } from '@angular/material/sort';
import { AgentService } from '../service/agent.service';

@Component({
  selector: 'app-performatives',
  templateUrl: './performatives.component.html',
  styleUrls: ['./performatives.component.css']
})
export class PerformativesComponent implements OnInit {

  constructor(public agentService : AgentService) { }

  ngOnInit(): void {
    this.agentService.getPerformatives();
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
