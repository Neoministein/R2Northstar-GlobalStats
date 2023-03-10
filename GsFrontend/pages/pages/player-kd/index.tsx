import { Column } from 'primereact/column';
import BackendService from '../../../demo/service/BackendService';
import TopResultTable from '../../../demo/components/TopResultTable';
import { PlayerKdBucket } from '../../../demo/service/BackendService';

const WinRatioPage = () => {

    const winRatioBody = (column: PlayerKdBucket) => {
        if(column?.kd) {
            return column?.kd.toFixed(3) + "%";
        }
        return null;
    }

    return (
        <TopResultTable 
            title="Top Player K/D" 
            getGlobalRanking={(tags) => BackendService.getTopPlayerKd(tags)}
            columns={
                [<Column header="Player Name" field="playerName" />,
                <Column header="K/D" body={winRatioBody} />,
                <Column header="Kills"  field="PGS_PILOT_KILLS" />,
                <Column header="Deaths" field="PGS_DEATHS" />]}/> 
    );
};

export default WinRatioPage;