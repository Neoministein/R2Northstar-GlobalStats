import { Column } from "primereact/column";
import { DataTable } from "primereact/datatable";
import { Skeleton } from "primereact/skeleton";
import { VirtualScrollerLazyEvent, VirtualScrollerLoadingTemplateOptions } from "primereact/virtualscroller";
import { useState } from "react";
import BackendService, { PlayerBucket} from "../service/BackendService";

interface TableProps {
    getGlobalRanking(): Promise<any>
    columns: JSX.Element[]
    title: string
}

export default function TopResultTable<T extends PlayerBucket>({getGlobalRanking, columns, title} : TableProps) { 
    const [globalRanking, setGlobalRanking] = useState<T[]>([]);

    const [virtualRanking, setVirtualRanking] = useState<T[]>([]);
    const [lazyLoading, setLazyLoading] = useState<boolean>(false);

    const loadRankingLazy = async (event: VirtualScrollerLazyEvent) => {
        !lazyLoading && setLazyLoading(true);

        const first = event.first as number;
        const last = event.last as number;
    
        let _virtualRanking;
        let _globalRanking;
        

        if(virtualRanking.length === 0) {
            _globalRanking = await getGlobalRanking();

            setGlobalRanking(_globalRanking);
            _virtualRanking = Array.from({ length: _globalRanking.length});
        } else {
            _virtualRanking = [...virtualRanking];
            _globalRanking = globalRanking;
        }
        

        const loadedRanking = await Promise.all(_globalRanking.slice(first as number, last as number).map(
            row => BackendService.getPlayerNameFromUid(row.key).then(playerLookUp => {
                row.playerName = playerLookUp.playerName;
                return row;
            })
        ));
        Array.prototype.splice.apply(_virtualRanking, [...[first, last as number - (first as number)], ...loadedRanking]);
        setVirtualRanking(_virtualRanking);
        setLazyLoading(false);
    };

    const loadingTemplate = (options: VirtualScrollerLoadingTemplateOptions) => {
        return (
            <div className="flex align-items-center" style={{ height: '17px', flexGrow: '1', overflow: 'hidden' }}>
                <Skeleton width={options.cellEven ? (options.field === 'year' ? '30%' : '40%') : '60%'} height="1rem" />
            </div>
        );
    };

    return (
        <div className="grid">
            <div className="col-12">
                <div className="card" style={{height: "calc(100vh - 9.5rem)"}}>
                    <h5>{title}</h5>
                    <div style={{height: "100%"}}>
                    <DataTable value={virtualRanking} scrollable scrollHeight={"flex"} style={{paddingBottom: "20px"}}
                            virtualScrollerOptions={{ lazy: true, onLazyLoad: loadRankingLazy, itemSize: 46, delay: 0, showLoader: true, loading: lazyLoading, loadingTemplate, step: 50 }}
                            tableStyle={{ minWidth: '50rem' }} key={"key"}>
                                {columns}
                    </DataTable>
                    </div>
                </div>
            </div>
        </div>
    );
}