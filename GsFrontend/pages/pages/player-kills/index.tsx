import { Column } from 'primereact/column';
import TopService from '../../../demo/service/TopService';
import TopResultTable from '../../../demo/components/TopResultTable';

const PlayerKillPage = () => {

    return (
        <TopResultTable 
            title="Top Player Kills" 
            getGlobalRanking={(tags) => TopService.getTopPlayerKills(tags)}
            columns={
                [<Column field="playerName" header="PlayerName"/>,
                <Column field="PGS_PILOT_KILLS" header="Player Kills"/>]}/> 
    );
};

export default PlayerKillPage;