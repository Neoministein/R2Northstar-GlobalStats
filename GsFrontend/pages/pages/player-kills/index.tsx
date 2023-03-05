import { Column } from 'primereact/column';
import BackendService from '../../../demo/service/BackendService';
import TopResultTable from '../../../demo/components/TopResultTable';

const PlayerKillPage = () => {

    return (
        <TopResultTable 
            title="Top Player Kills" 
            getGlobalRanking={(tags) => BackendService.getTopPlayerKills(tags)}
            columns={
                [<Column field="playerName" header="PlayerName"/>,
                <Column field="PGS_PILOT_KILLS" header="Player Kills"/>]}/> 
    );
};

export default PlayerKillPage;