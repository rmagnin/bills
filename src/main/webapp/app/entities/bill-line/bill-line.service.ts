import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IBillLine } from 'app/shared/model/bill-line.model';

type EntityResponseType = HttpResponse<IBillLine>;
type EntityArrayResponseType = HttpResponse<IBillLine[]>;

@Injectable({ providedIn: 'root' })
export class BillLineService {
  public resourceUrl = SERVER_API_URL + 'api/bill-lines';

  constructor(protected http: HttpClient) {}

  create(billLine: IBillLine): Observable<EntityResponseType> {
    return this.http.post<IBillLine>(this.resourceUrl, billLine, { observe: 'response' });
  }

  update(billLine: IBillLine): Observable<EntityResponseType> {
    return this.http.put<IBillLine>(this.resourceUrl, billLine, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBillLine>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBillLine[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
